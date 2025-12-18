package org.example.service.rules;

import org.example.model.Transaction;

public class HighAmountRule implements Rule {

    private final double amountThreshold;

    public HighAmountRule(double amountThreshold) {
        this.amountThreshold = amountThreshold;
    }

    @Override
    public boolean evaluate(Transaction transaction) {
        return transaction.getTransactionAmount() > amountThreshold;
    }

    @Override
    public String getRuleName() {
        return "HighAmountRule";
    }
}
