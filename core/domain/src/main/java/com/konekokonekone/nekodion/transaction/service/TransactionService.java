package com.konekokonekone.nekodion.transaction.service;

import com.konekokonekone.nekodion.support.exception.EntityNotFoundException;
import com.konekokonekone.nekodion.transaction.entity.Transaction;
import com.konekokonekone.nekodion.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;

    /**
     * ユーザーの入出金一覧取得
     *
     * @param userId ユーザーID
     * @return 入出金一覧
     */
    public List<Transaction> findByUserId(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    /**
     * 総資産を取得（収入合計 - 支出合計）
     *
     * @param userId ユーザーID
     * @return 総資産
     */
    public BigDecimal getTotalAssets(String userId) {
        var totalIncome = transactionRepository.sumIncome(userId);
        var totalExpense = transactionRepository.sumExpense(userId);
        return totalIncome.subtract(totalExpense);
    }

    /**
     * 入出金詳細取得
     *
     * @param id 入出金ID
     * @param userId ユーザーID
     * @return 入出金
     */
    public Transaction findByIdAndUserId(Long id, String userId) {
        return transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("入出金が見つかりません。入出金ID[%d]", id)));
    }
}
