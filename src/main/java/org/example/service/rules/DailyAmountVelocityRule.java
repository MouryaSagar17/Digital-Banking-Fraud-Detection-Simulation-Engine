package org.example.service.rules;

import org.example.model.Transaction;

public class DailyAmountVelocityRule implements Rule {

    private final double dailyAmountLimit;

    public DailyAmountVelocityRule(double dailyAmountLimit) {
        this.dailyAmountLimit = dailyAmountLimit;
    }

    @Override
    public boolean evaluate(Transaction transaction) {
        return transaction.getDailyTotalAmount() > dailyAmountLimit;
    }

    @Override
    public String getRuleName() {
        return "DailyAmountVelocityRule";
    }
}
