package org.example.controller;

import org.example.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final TransactionService transactionService;

    public AccountController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{accountId}/block")
    public ResponseEntity<Map<String, String>> blockAccount(@PathVariable String accountId, @RequestBody Map<String, String> payload) {
        String reason = payload.getOrDefault("reason", "Suspicious activity detected by analyst.");
        Long transactionId = Long.parseLong(payload.get("transactionId"));
        
        transactionService.blockAccount(accountId, transactionId, reason);
        return ResponseEntity.ok(Map.of("message", "Account " + accountId + " has been blocked."));
    }
}
