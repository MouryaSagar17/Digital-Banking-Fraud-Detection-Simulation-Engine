package org.example.service;

import org.example.dto.TransactionRequest;
import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repo;

    public TransactionService(TransactionRepository repo) {
        this.repo = repo;
    }

    public Transaction validateAndSave(TransactionRequest req) {

        // amount rule
        if (req.getTransactionAmount() <= 0 || req.getTransactionAmount() > 200000) {
            throw new IllegalArgumentException("Transaction amount is invalid");
        }

        // currency rule
        if (!"INR".equalsIgnoreCase(req.getCurrency())) {
            throw new IllegalArgumentException("Only INR supported");
        }

        // channel rule
        List<String> allowed = List.of("WEB", "MOBILE_APP", "UPI", "CARD_PRESENT");
        if (!allowed.contains(req.getChannel())) {
            throw new IllegalArgumentException("Invalid channel: " + req.getChannel());
        }

        // id format rule
        if (!req.getCustomerId().startsWith("CUST")) {
            throw new IllegalArgumentException("Customer ID must start with CUST");
        }
        if (!req.getAccountId().startsWith("ACC")) {
            throw new IllegalArgumentException("Account ID must start with ACC");
        }

        // map DTO → entity
        Transaction t = new Transaction();
        t.setCustomerId(req.getCustomerId());
        t.setAccountId(req.getAccountId());
        t.setTransactionAmount(req.getTransactionAmount());
        t.setCurrency(req.getCurrency().toUpperCase());
        t.setTxnTimestamp(req.getTxnTimestamp());
        t.setChannel(req.getChannel());
        t.setCountry(req.getCountry());
        t.setMerchantId(req.getMerchantId());
        t.setMerchantCategory(req.getMerchantCategory());
        t.setIpAddress(req.getIpAddress());
        t.setIpRiskScore(req.getIpRiskScore());
        t.setRuleRiskScore(req.getRuleRiskScore());
        t.setStatus("AUTHORIZED");
        t.setIsFraudLabel(req.getIsFraudLabel());

        return repo.save(t);
    }
}
