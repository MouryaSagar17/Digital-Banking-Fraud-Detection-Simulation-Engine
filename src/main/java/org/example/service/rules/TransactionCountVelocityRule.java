package org.example.service.rules;

import org.example.model.Transaction;

public class TransactionCountVelocityRule implements Rule {

    private final int dailyCountLimit;

    public TransactionCountVelocityRule(int dailyCountLimit) {
        this.dailyCountLimit = dailyCountLimit;
    }

    @Override
    public boolean evaluate(Transaction transaction) {
        return transaction.getDailyTransactionCount() > dailyCountLimit;
    }

    @Override
    public String getRuleName() {
        return "TransactionCountVelocityRule";
    }
}
