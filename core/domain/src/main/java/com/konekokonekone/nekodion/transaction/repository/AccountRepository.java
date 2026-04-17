package com.konekokonekone.nekodion.transaction.repository;

import com.konekokonekone.nekodion.transaction.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("""
            SELECT
                a
            FROM
                Account a
            LEFT JOIN FETCH
                a.transactions
            WHERE
                a.userId = :userId
            """)
    List<Account> findByUserIdWithTransactions(String userId);

    @Query("""
            SELECT
                a
            FROM
                Account a
            LEFT JOIN FETCH
                a.transactions
            LEFT JOIN FETCH
                a.accountTemplate
            WHERE
                a.id = :id
                AND a.userId = :userId
            """)
    Optional<Account> findByIdAndUserIdWithTransactions(Long id, String userId);
}
