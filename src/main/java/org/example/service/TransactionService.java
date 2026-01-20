package org.example.service;

import org.example.auth.EmailService;
import org.example.dto.DashboardStats;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        // SIMULATION LOGIC: Ensure ACC_BLOCKED_USER is actually blocked
        if ("ACC_BLOCKED_USER".equals(transaction.getAccountId())) {
            if (blockedAccountRepository.findByAccountId("ACC_BLOCKED_USER").isEmpty()) {
                blockedAccountRepository.save(new BlockedAccount("ACC_BLOCKED_USER", "Simulated Block", null));
            }
        }

        // Check if account is blocked
        if (blockedAccountRepository.findByAccountId(transaction.getAccountId()).isPresent()) {
            transaction.setStatus("BLOCKED_ACCOUNT");
            transaction.setFraudLabel(true);
            Transaction finalTransaction = transactionRepository.save(transaction);
            messagingTemplate.convertAndSend("/topic/transactions", finalTransaction);
            return finalTransaction;
        }

        // 1. Save the initial transaction with PENDING status
        Transaction savedTransaction = transactionRepository.save(transaction);

        // SIMULATION LOGIC: Force PENDING status for specific test account
        if ("ACC_PENDING_USER".equals(transaction.getAccountId())) {
            messagingTemplate.convertAndSend("/topic/transactions", savedTransaction);
            return savedTransaction;
        }

        // 2. Evaluate the transaction against the rules
        List<String> triggeredRules = ruleEngine.evaluate(savedTransaction);
        int ruleScore = ruleEngine.calculateRiskScore(triggeredRules);

        // 3. ML Prediction
        String mlPrediction = fraudPredictor.predict(savedTransaction);
        double mlScore = fraudPredictor.getFraudProbability(savedTransaction);
        
        savedTransaction.setMlPrediction(mlPrediction);
        savedTransaction.setMlScore(mlScore);

        // 4. Final Decision Logic
        String finalStatus = "NORMAL";

        if (triggeredRules.isEmpty()) {
            finalStatus = "NORMAL";
        } else if (triggeredRules.contains("BlacklistRule")) {
            finalStatus = "FRAUD";
        } else if (triggeredRules.size() <= 2 && mlScore < 0.5) {
            finalStatus = "PENDING"; // 1-2 rules AND low ML score -> Pending
        } else {
            finalStatus = "FRAUD"; // >2 rules OR high ML score with rules -> Fraud
        }

        // ML Override: If ML is extremely confident (> 0.8), escalate to FRAUD regardless of rules
        if (mlScore > 0.8) {
            finalStatus = "FRAUD";
            if (!triggeredRules.contains("ML_High_Confidence_Fraud")) {
                triggeredRules.add("ML_High_Confidence_Fraud");
            }
        }

        // 5. Update Transaction
        savedTransaction.setStatus(finalStatus);
        savedTransaction.setRiskRuleFlags(String.join(", ", triggeredRules));
        savedTransaction.setRuleRiskScore(ruleScore);
        savedTransaction.setFraudLabel("FRAUD".equals(finalStatus));

        Transaction finalTransaction = transactionRepository.save(savedTransaction);

        // 6. Trigger Actions
        if ("FRAUD".equals(finalStatus)) {
            emailService.sendBlockNotificationEmail(adminEmail, finalTransaction);
        }

        // 7. Broadcast Update
        messagingTemplate.convertAndSend("/topic/transactions", finalTransaction);

        return finalTransaction;
    }

    public Page<Transaction> getAllTransactions(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    public List<Transaction> getAllTransactionsList() {
        return transactionRepository.findAll();
    }
    
    public Page<Transaction> searchTransactions(List<String> statuses, String search, Pageable pageable) {
        return transactionRepository.searchTransactions(statuses, search, pageable);
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
    
    public List<Transaction> getTransactionsByCountry(String country) {
        return transactionRepository.findByCountry(country);
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
            // Determine expiry based on reason (simple heuristic for now)
            LocalDateTime expiry = null;
            if (reason != null && reason.contains("24h")) {
                expiry = LocalDateTime.now().plusHours(24);
            }
            
            BlockedAccount blockedAccount = new BlockedAccount(accountId, reason, expiry);
            blockedAccountRepository.save(blockedAccount);
            
            transactionRepository.findById(transactionId).ifPresent(transaction -> {
                emailService.sendBlockNotificationEmail(adminEmail, transaction);
            });
        }
    }

    public DashboardStats getDashboardStats(String country) {
        List<Transaction> transactions;
        if (country != null && !country.equals("ALL")) {
            transactions = transactionRepository.findByCountry(country);
        } else {
            transactions = transactionRepository.findAll();
        }

        DashboardStats stats = new DashboardStats();
        stats.setTotalTransactions(transactions.size());
        stats.setTotalVolume(transactions.stream().mapToDouble(Transaction::getTransactionAmount).sum());

        Map<String, Long> riskCounts = new HashMap<>();
        Map<String, Double> riskVolumes = new HashMap<>();
        Map<String, Map<String, Long>> countryRiskStats = new HashMap<>();

        riskCounts.put("LOW", 0L);
        riskCounts.put("MED", 0L);
        riskCounts.put("HIGH", 0L);
        riskVolumes.put("LOW", 0.0);
        riskVolumes.put("MED", 0.0);
        riskVolumes.put("HIGH", 0.0);

        for (Transaction t : transactions) {
            String riskLevel = "LOW";
            if (t.getStatus().equals("SUSPICIOUS") || t.getStatus().equals("PENDING")) riskLevel = "MED";
            if (t.getStatus().equals("FRAUD") || t.getStatus().equals("CONFIRMED_FRAUD") || t.getStatus().contains("BLOCKED")) riskLevel = "HIGH";

            riskCounts.put(riskLevel, riskCounts.get(riskLevel) + 1);
            riskVolumes.put(riskLevel, riskVolumes.get(riskLevel) + t.getTransactionAmount());

            countryRiskStats.putIfAbsent(t.getCountry(), new HashMap<>());
            Map<String, Long> cStats = countryRiskStats.get(t.getCountry());
            cStats.put(riskLevel, cStats.getOrDefault(riskLevel, 0L) + 1);
        }

        stats.setRiskCounts(riskCounts);
        stats.setRiskVolumes(riskVolumes);
        stats.setCountryRiskStats(countryRiskStats);

        return stats;
    }
}
