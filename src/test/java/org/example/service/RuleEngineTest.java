package org.example.service;

import org.example.model.Transaction;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RuleEngineTest {

    private final ComprehensiveRuleEngine ruleEngine = new ComprehensiveRuleEngine();

    @Test
    void testHighAmountRule() {
        Transaction t = new Transaction();
        t.setTransactionAmount(60000.0); // Above threshold
        t.setIpRiskScore(0);
        t.setChannel("WEB");
        t.setCountry("USA");
        t.setUserAccountCountry("USA");

        List<String> triggered = ruleEngine.evaluate(t);
        assertTrue(triggered.contains("HighAmountRule"), "Should trigger HighAmountRule");
    }

    @Test
    void testNormalTransaction() {
        Transaction t = new Transaction();
        t.setTransactionAmount(100.0);
        t.setIpRiskScore(0);
        t.setChannel("WEB");
        t.setCountry("USA");
        t.setUserAccountCountry("USA");

        List<String> triggered = ruleEngine.evaluate(t);
        assertTrue(triggered.isEmpty(), "Should trigger no rules");
        
        int score = ruleEngine.calculateRiskScore(triggered);
        assertEquals("NORMAL", ruleEngine.getFinalStatus(triggered, score));
    }
}
