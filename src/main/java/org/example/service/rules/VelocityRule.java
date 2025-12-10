package org.example.service.rules;

import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import java.time.LocalDateTime;
import java.util.List;

public class VelocityRule implements Rule {

    private final TransactionRepository transactionRepository;
    private static final int MAX_TRANSACTIONS = 3;
    private static final int TIME_WINDOW_MINUTES = 5;

    public VelocityRule(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public boolean evaluate(Transaction transaction) {
        LocalDateTime fiveMinutesAgo = transaction.getTxnTimestamp().minusMinutes(TIME_WINDOW_MINUTES);
        List<Transaction> recentTransactions = transactionRepository.findByAccountId(transaction.getAccountId());

        long count = recentTransactions.stream()
                .filter(t -> t.getTxnTimestamp().isAfter(fiveMinutesAgo))
                .count();

        return count > MAX_TRANSACTIONS;
    }

    @Override
    public String getRuleName() {
        return "VelocityRule";
    }
}
