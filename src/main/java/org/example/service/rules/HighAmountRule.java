package org.example.service.rules;

import org.example.model.Transaction;

public class HighAmountRule implements Rule {

    private static final double AMOUNT_THRESHOLD = 10000.0;

    @Override
    public boolean evaluate(Transaction transaction) {
        return transaction.getTransactionAmount() > AMOUNT_THRESHOLD;
    }

    @Override
    public String getRuleName() {
        return "HighAmountRule";
    }
}
