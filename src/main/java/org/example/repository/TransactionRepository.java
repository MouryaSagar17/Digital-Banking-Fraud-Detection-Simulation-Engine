package org.example.repository;

import org.example.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByCustomerId(String customerId);
    List<Transaction> findByAccountId(String accountId);
    List<Transaction> findByIsFraudLabel(boolean isFraudLabel);
    List<Transaction> findByTxnTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Transaction> findByRuleRiskScoreGreaterThan(int score);
    List<Transaction> findByRiskRuleFlagsContaining(String ruleName);
    
    // For Dashboard Stats
    List<Transaction> findByCountry(String country);

    // Combined Search and Filter
    @Query("SELECT t FROM Transaction t WHERE " +
           "((:statuses) IS NULL OR t.status IN (:statuses)) AND " +
           "(:search IS NULL OR t.accountId LIKE %:search% OR CAST(t.id AS string) LIKE %:search%)")
    Page<Transaction> searchTransactions(@Param("statuses") List<String> statuses, 
                                         @Param("search") String search, 
                                         Pageable pageable);
}
