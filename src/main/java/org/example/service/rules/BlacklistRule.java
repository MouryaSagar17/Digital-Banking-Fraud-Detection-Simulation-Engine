package org.example.service.rules;

import org.example.model.Transaction;

public class BlacklistRule implements Rule {

    @Override
    public boolean evaluate(Transaction transaction) {
        return transaction.isBlacklisted();
    }

    @Override
    public String getRuleName() {
        return "BlacklistRule";
    }
}
