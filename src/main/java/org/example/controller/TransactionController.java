package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.TransactionRequest;
import org.example.model.Transaction;
import org.example.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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
        // Set initial status to PENDING
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

        // Populate new fields with random data for simulation
        transaction.setIpRiskScore(random.nextInt(101));
        transaction.setDeviceFingerprint(UUID.randomUUID().toString());
        transaction.setNewDevice(random.nextBoolean());
        transaction.setUserAccountCountry(random.nextBoolean() ? "IN" : "US");
        transaction.setTimeSinceLastTransaction(random.nextInt(3600));
        transaction.setDailyTotalAmount(random.nextDouble() * 200000);
        transaction.setDailyTransactionCount(random.nextInt(20));
        transaction.setDormant(random.nextBoolean());
        transaction.setBlacklisted(random.nextDouble() < 0.05); // 5% chance of being blacklisted

        return transactionService.processTransaction(transaction);
    }

    @GetMapping
    public List<Transaction> getAllTransactions(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return transactionService.getTransactionsByDateRange(startDate, endDate);
        }
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id)
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
