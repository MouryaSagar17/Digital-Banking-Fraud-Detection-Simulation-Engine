package org.example.service.rules;

import org.example.model.Transaction;

public class IpRiskScoreRule implements Rule {

    private final int riskThreshold;

    public IpRiskScoreRule(int riskThreshold) {
        this.riskThreshold = riskThreshold;
    }

    @Override
    public boolean evaluate(Transaction transaction) {
        return transaction.getIpRiskScore() > riskThreshold;
    }

    @Override
    public String getRuleName() {
        return "IpRiskScoreRule";
    }
}
