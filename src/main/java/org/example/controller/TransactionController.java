package org.example.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.dto.TransactionRequest;
import org.example.model.Transaction;
import org.example.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final Random random = new Random();

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public Transaction createTransaction(@Valid @RequestBody TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();
        transaction.setStatus("PENDING");
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

        // INTELLIGENT SIMULATION DATA POPULATION
        boolean isNormal = transactionRequest.getCustomerId().startsWith("CUST_NORMAL");
        boolean isFraud = transactionRequest.getCustomerId().startsWith("CUST_FRAUD");

        if (isNormal) {
            transaction.setIpRiskScore(random.nextInt(10));
            transaction.setNewDevice(false);
            transaction.setUserAccountCountry(transactionRequest.getCountry());
            transaction.setTimeSinceLastTransaction(10000 + random.nextInt(50000));
            transaction.setDailyTotalAmount(transactionRequest.getTransactionAmount() + random.nextInt(100));
            transaction.setDailyTransactionCount(1 + random.nextInt(3));
            transaction.setDormant(false);
            transaction.setBlacklisted(false);
        } else if (isFraud) {
            transaction.setIpRiskScore(70 + random.nextInt(30));
            transaction.setNewDevice(true);
            transaction.setUserAccountCountry("UNK");
            transaction.setTimeSinceLastTransaction(random.nextInt(60));
            transaction.setDailyTotalAmount(100000 + random.nextDouble() * 50000);
            transaction.setDailyTransactionCount(10 + random.nextInt(20));
            transaction.setDormant(random.nextBoolean());
            transaction.setBlacklisted(random.nextDouble() < 0.3);
        } else {
            transaction.setIpRiskScore(random.nextInt(101));
            transaction.setNewDevice(random.nextBoolean());
            transaction.setUserAccountCountry(random.nextBoolean() ? "IN" : "US");
            transaction.setTimeSinceLastTransaction(random.nextInt(3600));
            transaction.setDailyTotalAmount(random.nextDouble() * 200000);
            transaction.setDailyTransactionCount(random.nextInt(20));
            transaction.setDormant(random.nextBoolean());
            transaction.setBlacklisted(random.nextDouble() < 0.05);
        }

        transaction.setDeviceFingerprint(UUID.randomUUID().toString());

        return transactionService.processTransaction(transaction);
    }

    @GetMapping
    public List<Transaction> getAllTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return transactionService.getTransactionsByDateRange(startDate, endDate);
        }
        return transactionService.getAllTransactionsList();
    }

    @GetMapping("/paged")
    public Page<Transaction> getPagedTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String riskLevel,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String search) {
        
        Sort sort = Sort.by("txnTimestamp").descending();
        
        if ("risk_desc".equals(sortBy)) {
            sort = Sort.by("ruleRiskScore").descending();
        } else if ("ml_desc".equals(sortBy)) {
            sort = Sort.by("mlScore").descending();
        } else if ("amount_desc".equals(sortBy)) {
            sort = Sort.by("transactionAmount").descending();
        } else if ("oldest".equals(sortBy)) {
            sort = Sort.by("txnTimestamp").ascending();
        }

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        
        List<String> statuses = new ArrayList<>();
        
        // Handle Risk Level Filter
        if (riskLevel != null && !riskLevel.equals("ALL")) {
            if (riskLevel.equals("HIGH")) {
                statuses.add("FRAUD");
                statuses.add("CONFIRMED_FRAUD");
                statuses.add("BLOCKED_ACCOUNT");
                statuses.add("BLOCKED_PERMANENT");
                statuses.add("BLOCKED_24H");
            } else if (riskLevel.equals("MED")) {
                statuses.add("SUSPICIOUS");
                statuses.add("PENDING");
            } else if (riskLevel.equals("LOW")) {
                statuses.add("NORMAL");
                statuses.add("CLEARED");
            }
        } else if (status != null && !status.equals("ALL")) {
            if (status.equals("SUCCESS")) {
                statuses.add("NORMAL");
                statuses.add("CLEARED");
            } else if (status.equals("FAILED")) {
                statuses.add("BLOCKED_ACCOUNT");
                statuses.add("BLOCKED_PERMANENT");
                statuses.add("BLOCKED_24H");
            } else if (status.equals("PENDING")) {
                statuses.add("PENDING");
            } else if (status.equals("FRAUD")) {
                statuses.add("FRAUD");
                statuses.add("SUSPICIOUS");
                statuses.add("CONFIRMED_FRAUD");
            } else if (status.equals("RESOLVED")) {
                statuses.add("CLEARED");
                statuses.add("CONFIRMED_FRAUD");
                statuses.add("BLOCKED_PERMANENT");
                statuses.add("BLOCKED_24H");
            } else if (status.equals("BLOCKED")) {
                statuses.add("BLOCKED_ACCOUNT");
                statuses.add("BLOCKED_PERMANENT");
                statuses.add("BLOCKED_24H");
            }
        }

        if (statuses.isEmpty()) {
            statuses = null; // Search all
        }
        
        return transactionService.searchTransactions(statuses, search, pageRequest);
    }

    @GetMapping("/export")
    public void exportTransactions(
            HttpServletResponse response,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) throws IOException {

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"transactions.csv\"");

        List<String> statuses = null; 
        // ... (Add status resolution logic here if needed for export consistency)

        PageRequest pageRequest = PageRequest.of(0, 10000, Sort.by("txnTimestamp").descending());
        Page<Transaction> page = transactionService.searchTransactions(statuses, search, pageRequest);
        List<Transaction> transactions = page.getContent();

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Transaction ID,Account ID,Amount,Currency,Status,Fraud Flag,Risk Score,Timestamp");
            for (Transaction t : transactions) {
                writer.printf("%d,%s,%.2f,%s,%s,%s,%s,%s%n",
                        t.getId(),
                        t.getAccountId(),
                        t.getTransactionAmount(),
                        t.getCurrency(),
                        t.getStatus(),
                        t.isFraudLabel() ? "YES" : "NO",
                        t.getRuleRiskScore(),
                        t.getTxnTimestamp());
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<Transaction> reviewTransaction(@PathVariable Long id, @RequestParam String status) {
        return transactionService.updateTransactionStatus(id, status)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
