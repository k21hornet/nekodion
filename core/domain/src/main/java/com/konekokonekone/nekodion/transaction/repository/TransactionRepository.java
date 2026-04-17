package com.konekokonekone.nekodion.transaction.repository;

import com.konekokonekone.nekodion.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * 入出金一覧取得
     */
    @Query("""
            SELECT
                t
            FROM
                Transaction t
            WHERE
                t.userId = :userId
            ORDER BY
                t.transactionDate DESC
            """)
    List<Transaction> findByUserId(String userId);

    Optional<Transaction> findByIdAndUserId(Long id, String userId);

    /**
     * 収入合計を取得
     */
    @Query("""
            SELECT
                COALESCE(SUM(t.amount), 0)
            FROM
                Transaction t
            WHERE
                t.userId = :userId
                AND t.transactionType IN ('INCOME')
            """)
    BigDecimal sumIncome(String userId);

    /**
     * 支出合計を取得
     */
    @Query("""
            SELECT
                COALESCE(SUM(t.amount), 0)
            FROM
                Transaction t
            WHERE
                t.userId = :userId
                AND t.transactionType IN ('EXPENSE')
            """)
    BigDecimal sumExpense(String userId);
}
