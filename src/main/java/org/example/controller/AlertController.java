package org.example.controller;

import org.example.model.Transaction;
import org.example.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final TransactionService transactionService;

    public AlertController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public List<Transaction> getFraudulentTransactions() {
        return transactionService.getFraudulentTransactions();
    }
}
