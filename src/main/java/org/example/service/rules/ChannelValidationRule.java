package org.example.service.rules;

import org.example.model.Transaction;
import java.util.Arrays;
import java.util.List;

public class ChannelValidationRule implements Rule {

    private static final List<String> ALLOWED_CHANNELS = Arrays.asList("WEB", "MOBILE", "POS");

    @Override
    public boolean evaluate(Transaction transaction) {
        return !ALLOWED_CHANNELS.contains(transaction.getChannel());
    }

    @Override
    public String getRuleName() {
        return "ChannelValidationRule";
    }
}
