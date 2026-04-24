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
                t.transactionDateTime DESC
            """)
    List<Transaction> findByUserId(String userId);

    Optional<Transaction> findByIdAndUserId(Long id, String userId);

    /**
     * CARDを除く収入合計を取得（総資産計算用）
     */
    @Query("""
            SELECT
                COALESCE(SUM(t.amount), 0)
            FROM
                Transaction t
            WHERE
                t.userId = :userId
                AND t.transactionType IN ('INCOME')
                AND t.account.accountType <> 'CARD'
            """)
    BigDecimal sumIncomeExcludingCard(String userId);

    /**
     * CARDを除く支出合計を取得（総資産計算用）
     */
    @Query("""
            SELECT
                COALESCE(SUM(t.amount), 0)
            FROM
                Transaction t
            WHERE
                t.userId = :userId
                AND t.transactionType IN ('EXPENSE')
                AND t.account.accountType <> 'CARD'
            """)
    BigDecimal sumExpenseExcludingCard(String userId);

    /**
     * 月次収入合計を取得
     */
    @Query("""
            SELECT
                COALESCE(SUM(t.amount), 0)
            FROM
                Transaction t
            WHERE
                t.userId = :userId
                AND t.transactionType IN ('INCOME')
                AND YEAR(t.transactionDateTime) = :year
                AND MONTH(t.transactionDateTime) = :month
            """)
    BigDecimal sumIncomeByMonth(String userId, int year, int month);

    /**
     * 月次支出合計を取得
     */
    @Query("""
            SELECT
                COALESCE(SUM(t.amount), 0)
            FROM
                Transaction t
            WHERE
                t.userId = :userId
                AND t.transactionType IN ('EXPENSE')
                AND YEAR(t.transactionDateTime) = :year
                AND MONTH(t.transactionDateTime) = :month
            """)
    BigDecimal sumExpenseByMonth(String userId, int year, int month);
}
