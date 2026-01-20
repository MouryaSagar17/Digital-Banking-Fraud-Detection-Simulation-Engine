package org.example.controller;

import org.example.dto.DashboardStats;
import org.example.model.Transaction;
import org.example.service.TransactionService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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
    public DashboardStats getStats(@RequestParam(required = false) String country) {
        return transactionService.getDashboardStats(country);
    }

    @GetMapping("/api/dashboard/recent")
    @ResponseBody
    public List<Transaction> getRecentTransactions() {
        return transactionService.getAllTransactions(PageRequest.of(0, 50, Sort.by("txnTimestamp").descending())).getContent();
    }
}
