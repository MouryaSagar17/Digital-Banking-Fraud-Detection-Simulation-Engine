package org.example.repository;

import org.example.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    // Existing methods
    List<Transaction> findByCustomerId(String customerId);
    List<Transaction> findByAccountId(String accountId);
    List<Transaction> findByIsFraudLabel(boolean isFraudLabel);
    List<Transaction> findByTxnTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Transaction> findByRuleRiskScoreGreaterThan(int score);
    List<Transaction> findByRiskRuleFlagsContaining(String ruleName);

    // New method for paginated filtering
    Page<Transaction> findByStatusIn(List<String> statuses, Pageable pageable);
}
