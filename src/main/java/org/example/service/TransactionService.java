package org.example.service;

import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final RuleEngine ruleEngine;

    public TransactionService(TransactionRepository transactionRepository, RuleEngine ruleEngine) {
        this.transactionRepository = transactionRepository;
        this.ruleEngine = ruleEngine;
    }

    public Transaction processTransaction(Transaction transaction) {
        // 1. Save the initial transaction
        Transaction savedTransaction = transactionRepository.save(transaction);

        // 2. Evaluate the transaction against the rules
        List<String> triggeredRules = ruleEngine.evaluate(savedTransaction);

        // 3. Update the transaction with the fraud check results
        if (!triggeredRules.isEmpty()) {
            savedTransaction.setStatus("FLAGGED");
            savedTransaction.setRiskRuleFlags(String.join(",", triggeredRules));
            savedTransaction.setFraudLabel(true);
        } else {
            savedTransaction.setStatus("CLEARED");
        }

        return transactionRepository.save(savedTransaction);
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
}
