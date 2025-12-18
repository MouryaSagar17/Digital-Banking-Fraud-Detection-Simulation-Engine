package org.example.controller;

import org.example.model.Transaction;
import org.example.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/api/dashboard/stats")
    @ResponseBody
    public Map<String, Long> getStats() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        long normal = transactions.stream().filter(t -> "NORMAL".equals(t.getStatus())).count();
        long fraud = transactions.size() - normal;
        Map<String, Long> stats = new HashMap<>();
        stats.put("normal", normal);
        stats.put("fraud", fraud);
        return stats;
    }
}
