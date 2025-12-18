package org.example.service.rules;

import org.example.model.Transaction;

public class DormantToActiveSpikeRule implements Rule {

    @Override
    public boolean evaluate(Transaction transaction) {
        return transaction.isDormant();
    }

    @Override
    public String getRuleName() {
        return "DormantToActiveSpikeRule";
    }
}
