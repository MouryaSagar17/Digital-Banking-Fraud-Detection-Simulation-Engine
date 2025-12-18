package org.example.service.rules;

import org.example.model.Transaction;

public class TransactionFrequencyRule implements Rule {

    private final int maxTransactions;
    private final int timeWindowSeconds;

    public TransactionFrequencyRule(int maxTransactions, int timeWindowSeconds) {
        this.maxTransactions = maxTransactions;
        this.timeWindowSeconds = timeWindowSeconds;
    }

    @Override
    public boolean evaluate(Transaction transaction) {
        // This rule requires historical data. For this implementation, we'll use
        // the timeSinceLastTransaction field as a proxy.
        return transaction.getTimeSinceLastTransaction() < timeWindowSeconds &&
               transaction.getDailyTransactionCount() > maxTransactions;
    }

    @Override
    public String getRuleName() {
        return "TransactionFrequencyRule";
    }
}
