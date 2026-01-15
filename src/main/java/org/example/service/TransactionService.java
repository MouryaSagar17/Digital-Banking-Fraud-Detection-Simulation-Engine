package org.example.service;

import org.example.auth.EmailService;
import org.example.ml.FraudPredictor;
import org.example.model.BlockedAccount;
import org.example.model.Transaction;
import org.example.repository.BlockedAccountRepository;
import org.example.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BlockedAccountRepository blockedAccountRepository;
    private final RuleEngine ruleEngine;
    private final SimpMessagingTemplate messagingTemplate;
    private final FraudPredictor fraudPredictor;
    private final EmailService emailService;

    @Value("${app.admin.email}")
    private String adminEmail;

    public TransactionService(TransactionRepository transactionRepository, BlockedAccountRepository blockedAccountRepository, RuleEngine ruleEngine, SimpMessagingTemplate messagingTemplate, FraudPredictor fraudPredictor, EmailService emailService) {
        this.transactionRepository = transactionRepository;
        this.blockedAccountRepository = blockedAccountRepository;
        this.ruleEngine = ruleEngine;
        this.messagingTemplate = messagingTemplate;
        this.fraudPredictor = fraudPredictor;
        this.emailService = emailService;
    }

    public Transaction processTransaction(Transaction transaction) {
        // 1. Check Blocked Account
        if (blockedAccountRepository.findByAccountId(transaction.getAccountId()).isPresent()) {
            transaction.setStatus("BLOCKED_ACCOUNT");
            transaction.setFraudLabel(true);
            transaction.setRiskRuleFlags("Account is manually blocked");
            Transaction finalTransaction = transactionRepository.save(transaction);
            messagingTemplate.convertAndSend("/topic/transactions", finalTransaction);
            return finalTransaction;
        }

        // 2. Initial Save
        Transaction savedTransaction = transactionRepository.save(transaction);

        // 3. Rule Engine Evaluation
        List<String> triggeredRules = ruleEngine.evaluate(savedTransaction);
        int ruleScore = ruleEngine.calculateRiskScore(triggeredRules);
        String ruleStatus = ruleEngine.getFinalStatus(triggeredRules);

        // 4. ML Prediction
        String mlPrediction = fraudPredictor.predict(savedTransaction);
        double mlScore = fraudPredictor.getFraudProbability(savedTransaction);
        
        savedTransaction.setMlPrediction(mlPrediction);
        savedTransaction.setMlScore(mlScore);

        // 5. Final Decision Logic
        String finalStatus = ruleStatus;

        // ML Override: If ML is very confident it's fraud, escalate PENDING to SUSPICIOUS/FRAUD
        if ("PENDING".equals(finalStatus) && "fraud".equalsIgnoreCase(mlPrediction) && mlScore > 0.8) {
            finalStatus = "FRAUD";
            triggeredRules.add("ML_High_Confidence_Fraud");
        }

        // 6. Update Transaction
        savedTransaction.setStatus(finalStatus);
        savedTransaction.setRiskRuleFlags(String.join(", ", triggeredRules)); // Save reasons
        savedTransaction.setRuleRiskScore(ruleScore);
        savedTransaction.setFraudLabel("FRAUD".equals(finalStatus));

        Transaction finalTransaction = transactionRepository.save(savedTransaction);

        // 7. Trigger Actions
        if ("FRAUD".equals(finalStatus)) {
            // Auto-alert for high confidence fraud
            emailService.sendBlockNotificationEmail(adminEmail, finalTransaction);
        }

        // 8. Broadcast Update
        messagingTemplate.convertAndSend("/topic/transactions", finalTransaction);

        return finalTransaction;
    }

    public Page<Transaction> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    public List<Transaction> getAllTransactionsList() {
        return transactionRepository.findAll();
    }
    
    public Page<Transaction> findByStatus(List<String> statuses, Pageable pageable) {
        return transactionRepository.findByStatusIn(statuses, pageable);
    }

    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getFraudulentTransactions() {
        return transactionRepository.findByIsFraudLabel(true);
    }

    public List<Transaction> getTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByTxnTimestampBetween(startDate, endDate);
    }

    public List<Transaction> getHighRiskTransactions(int threshold) {
        return transactionRepository.findByRuleRiskScoreGreaterThan(threshold);
    }

    public List<Transaction> getTransactionsByRule(String ruleName) {
        return transactionRepository.findByRiskRuleFlagsContaining(ruleName);
    }

    public Optional<Transaction> updateTransactionStatus(Long id, String newStatus) {
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setStatus(newStatus);
            if ("NORMAL".equalsIgnoreCase(newStatus) || "CLEARED".equalsIgnoreCase(newStatus)) {
                transaction.setFraudLabel(false);
            } else if ("FRAUD".equalsIgnoreCase(newStatus) || "CONFIRMED_FRAUD".equalsIgnoreCase(newStatus)) {
                transaction.setFraudLabel(true);
            }
            
            Transaction updated = transactionRepository.save(transaction);
            messagingTemplate.convertAndSend("/topic/transactions", updated);
            return updated;
        });
    }

    public void blockAccount(String accountId, Long transactionId, String reason) {
        if (blockedAccountRepository.findByAccountId(accountId).isEmpty()) {
            BlockedAccount blockedAccount = new BlockedAccount(accountId, reason);
            blockedAccountRepository.save(blockedAccount);
            
            transactionRepository.findById(transactionId).ifPresent(transaction -> {
                emailService.sendBlockNotificationEmail(adminEmail, transaction);
            });
        }
    }
}
