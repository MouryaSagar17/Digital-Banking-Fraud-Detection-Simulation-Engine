package org.example.service.rules;

import org.example.model.Transaction;

public class NewDeviceRule implements Rule {

    @Override
    public boolean evaluate(Transaction transaction) {
        return transaction.isNewDevice();
    }

    @Override
    public String getRuleName() {
        return "NewDeviceRule";
    }
}
