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

    public String getFinalStatus(List<String> triggeredRules) {
        if (triggeredRules.isEmpty()) {
            return "NORMAL";
        } else if (triggeredRules.size() <= 2) {
            return "SUSPICIOUS";
        } else {
            return "FRAUD";
        }
    }
}
