package org.example.service;

import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final RuleEngine ruleEngine;
    private final SimpMessagingTemplate messagingTemplate;

    public TransactionService(TransactionRepository transactionRepository, RuleEngine ruleEngine, SimpMessagingTemplate messagingTemplate) {
        this.transactionRepository = transactionRepository;
        this.ruleEngine = ruleEngine;
        this.messagingTemplate = messagingTemplate;
    }

    public Transaction processTransaction(Transaction transaction) {
        // 1. Save the initial transaction with PENDING status
        Transaction savedTransaction = transactionRepository.save(transaction);

        // 2. Evaluate the transaction against the rules
        List<String> triggeredRules = ruleEngine.evaluate(savedTransaction);
        String finalStatus = ruleEngine.getFinalStatus(triggeredRules);

        // 3. Update the transaction with the fraud check results
        savedTransaction.setStatus(finalStatus);
        savedTransaction.setRiskRuleFlags(String.join(",", triggeredRules));
        savedTransaction.setFraudLabel(!"NORMAL".equals(finalStatus));

        Transaction finalTransaction = transactionRepository.save(savedTransaction);

        // 4. Send the processed transaction to the WebSocket topic
        messagingTemplate.convertAndSend("/topic/transactions", finalTransaction);

        return finalTransaction;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
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
}
