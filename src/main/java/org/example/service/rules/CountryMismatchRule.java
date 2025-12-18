package org.example.service.rules;

import org.example.model.Transaction;

public class CountryMismatchRule implements Rule {

    @Override
    public boolean evaluate(Transaction transaction) {
        // This is a simplified check. A real implementation would have a more
        // sophisticated way of determining if a country is high-risk.
        return !transaction.getCountry().equals(transaction.getUserAccountCountry());
    }

    @Override
    public String getRuleName() {
        return "CountryMismatchRule";
    }
}
