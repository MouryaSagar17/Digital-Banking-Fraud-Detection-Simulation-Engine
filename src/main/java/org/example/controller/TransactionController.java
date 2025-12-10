package org.example.controller;

import org.example.dto.TransactionRequest;
import org.example.model.Transaction;
import org.example.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public Transaction createTransaction(@RequestBody TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();
        transaction.setCustomerId(transactionRequest.getCustomerId());
        transaction.setAccountId(transactionRequest.getAccountId());
        transaction.setTransactionAmount(transactionRequest.getTransactionAmount());
        transaction.setCurrency(transactionRequest.getCurrency());
        transaction.setTxnTimestamp(LocalDateTime.now());
        transaction.setChannel(transactionRequest.getChannel());
        transaction.setCountry(transactionRequest.getCountry());
        transaction.setMerchantId(transactionRequest.getMerchantId());
        transaction.setMerchantCategory(transactionRequest.getMerchantCategory());
        transaction.setIpAddress(transactionRequest.getIpAddress());

        return transactionService.processTransaction(transaction);
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
