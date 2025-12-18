package org.example.service.rules;

import org.example.model.Transaction;
import java.time.LocalTime;

public class UnusualTimeOfDayRule implements Rule {

    private final LocalTime startTime;
    private final LocalTime endTime;

    public UnusualTimeOfDayRule(int startHour, int endHour) {
        this.startTime = LocalTime.of(startHour, 0);
        this.endTime = LocalTime.of(endHour, 0);
    }

    @Override
    public boolean evaluate(Transaction transaction) {
        LocalTime transactionTime = transaction.getTxnTimestamp().toLocalTime();
        return transactionTime.isAfter(startTime) || transactionTime.isBefore(endTime);
    }

    @Override
    public String getRuleName() {
        return "UnusualTimeOfDayRule";
    }
}
