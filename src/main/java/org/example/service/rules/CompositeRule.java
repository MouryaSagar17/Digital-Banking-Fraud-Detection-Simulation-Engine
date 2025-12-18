package org.example.service.rules;

import java.util.ArrayList;
import java.util.List;

public class CompositeRule {

    private final List<Rule> rules;

    public CompositeRule() {
        rules = new ArrayList<>();
        // Basic Rules
        rules.add(new HighAmountRule(50000.0)); // Rule 1
        rules.add(new IpRiskScoreRule(30)); // Rule 2
        rules.add(new ChannelValidationRule()); // Rule 4
        rules.add(new CountryMismatchRule()); // Rule 5
        rules.add(new TransactionFrequencyRule(10, 60)); // Rule 7 (10 txns in 60 secs)
        rules.add(new DailyAmountVelocityRule(100000.0)); // Rule 9
        rules.add(new TransactionCountVelocityRule(5)); // Rule 10
        rules.add(new UnusualTimeOfDayRule(22, 6)); // Rule 11 (10 PM to 6 AM)
        rules.add(new NewDeviceRule()); // Rule 13
        rules.add(new DormantToActiveSpikeRule()); // Rule 19
        rules.add(new BlacklistRule()); // Rule 24
    }

    public List<Rule> getRules() {
        return rules;
    }
}
