package org.example.service.rules;

import org.example.model.Transaction;

public interface Rule {
    boolean evaluate(Transaction transaction);
    String getRuleName();
}
