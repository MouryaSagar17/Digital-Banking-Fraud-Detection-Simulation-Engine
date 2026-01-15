package org.example.service;

import org.example.model.Transaction;
import org.example.service.rules.CompositeRule;
import org.example.service.rules.Rule;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleEngine {

    private final List<Rule> rules;

    public RuleEngine() {
        this.rules = new CompositeRule().getRules();
    }

    public List<String> evaluate(Transaction transaction) {
        return rules.stream()
                .filter(rule -> rule.evaluate(transaction))
                .map(Rule::getRuleName)
                .collect(Collectors.toList());
    }

    public int calculateRiskScore(List<String> triggeredRules) {
        int score = triggeredRules.size() * 20;
        if (triggeredRules.contains("HighAmountRule")) score += 30;
        if (triggeredRules.contains("BlacklistRule")) score += 50;
        return Math.min(score, 100);
    }

    public String getFinalStatus(List<String> triggeredRules) {
        if (triggeredRules.isEmpty()) {
            return "NORMAL";
        } else if (triggeredRules.size() <= 2 && !triggeredRules.contains("BlacklistRule")) {
            return "PENDING"; // 1-2 rules -> Pending Review
        } else {
            return "FRAUD"; // >2 rules or Blacklist -> Fraud
        }
    }
}
