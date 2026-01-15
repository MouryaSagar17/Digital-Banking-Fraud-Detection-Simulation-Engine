package org.example.controller;

import org.example.model.Transaction;
import org.example.service.TransactionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final TransactionService transactionService;

    public DashboardController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/api/dashboard/stats")
    @ResponseBody
    public Map<String, Long> getStats() {
        List<Transaction> transactions = transactionService.getAllTransactionsList();
        long normal = transactions.stream().filter(t -> "NORMAL".equals(t.getStatus()) || "CLEARED".equals(t.getStatus())).count();
        long fraud = transactions.size() - normal;
        Map<String, Long> stats = new HashMap<>();
        stats.put("normal", normal);
        stats.put("fraud", fraud);
        return stats;
    }

    @GetMapping("/api/dashboard/recent")
    @ResponseBody
    public List<Transaction> getRecentTransactions() {
        // Use the paginated method to get the most recent 50
        return transactionService.getAllTransactions(PageRequest.of(0, 50, Sort.by("txnTimestamp").descending())).getContent();
    }
}
