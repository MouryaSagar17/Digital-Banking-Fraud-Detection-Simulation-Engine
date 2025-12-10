package org.example.service;

import org.example.model.Transaction;
import org.example.repository.TransactionRepository;
import org.example.service.rules.HighAmountRule;
import org.example.service.rules.Rule;
import org.example.service.rules.VelocityRule;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RuleEngine {

    private final List<Rule> rules;

    public RuleEngine(TransactionRepository transactionRepository) {
        rules = new ArrayList<>();
        rules.add(new HighAmountRule());
        rules.add(new VelocityRule(transactionRepository));
    }

    public List<String> evaluate(Transaction transaction) {
        return rules.stream()
                .filter(rule -> rule.evaluate(transaction))
                .map(Rule::getRuleName)
                .collect(Collectors.toList());
    }
}
