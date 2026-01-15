package org.example.repository;

import org.example.model.BlockedAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockedAccountRepository extends JpaRepository<BlockedAccount, Long> {
    Optional<BlockedAccount> findByAccountId(String accountId);
}
