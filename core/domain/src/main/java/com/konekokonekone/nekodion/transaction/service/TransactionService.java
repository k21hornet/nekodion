package com.konekokonekone.nekodion.transaction.service;

import com.konekokonekone.nekodion.category.service.CategoryMappingService;
import com.konekokonekone.nekodion.category.service.CategoryService;
import com.konekokonekone.nekodion.support.exception.EntityNotFoundException;
import com.konekokonekone.nekodion.transaction.dto.MonthlySummaryDto;
import com.konekokonekone.nekodion.transaction.dto.TransactionRequestDto;
import com.konekokonekone.nekodion.transaction.entity.Transaction;
import com.konekokonekone.nekodion.transaction.enums.TransactionType;
import com.konekokonekone.nekodion.transaction.repository.AccountRepository;
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

    private final AccountRepository accountRepository;

    private final CategoryService categoryService;

    private final CategoryMappingService categoryMappingService;

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
     * 総資産を取得（CARD口座を除く収入合計 - 支出合計）
     *
     * @param userId ユーザーID
     * @return 総資産
     */
    public BigDecimal getTotalAssets(String userId) {
        var totalIncome = transactionRepository.sumIncomeExcludingCard(userId);
        var totalExpense = transactionRepository.sumExpenseExcludingCard(userId);
        return totalIncome.subtract(totalExpense);
    }

    /**
     * 月次収支を取得
     *
     * @param userId ユーザーID
     * @param year   年
     * @param month  月
     * @return 月次収支
     */
    public MonthlySummaryDto getMonthlySummary(String userId, int year, int month) {
        var totalIncome = transactionRepository.sumIncomeByMonth(userId, year, month);
        var totalExpense = transactionRepository.sumExpenseByMonth(userId, year, month);
        return new MonthlySummaryDto(year, month, totalIncome, totalExpense);
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

    /**
     * 入出金記録
     *
     * @param userId ユーザーID
     * @param dto 入出金記録リクエスト
     */
    public void createTransaction(String userId, TransactionRequestDto dto) {
        var account = accountRepository.findByIdAndUserId(dto.getAccountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("口座が見つかりません。口座ID[%d]", dto.getAccountId())));
        var category = categoryService.findAccessibleById(dto.getCategoryId(), userId);

        var transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setTransactionType(TransactionType.codeOf(dto.getTransactionType()));
        transaction.setTransactionName(dto.getTransactionName());
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionDateTime(dto.getTransactionDateTime());
        transaction.setDescription(dto.getDescription());
        transaction.setIsAggregated(true);
        transaction.setIsConfirmed(true);

        transactionRepository.save(transaction);
    }

    /**
     * 入出金更新
     *
     * @param id 入出金ID
     * @param userId ユーザーID
     * @param dto 入出金更新リクエスト
     */
    public void updateTransaction(Long id, String userId, TransactionRequestDto dto) {
        var transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("入出金が見つかりません。入出金ID[%d]", id)));
        var account = accountRepository.findByIdAndUserId(dto.getAccountId(), userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("口座が見つかりません。口座ID[%d]", dto.getAccountId())));
        var newCategory = categoryService.findAccessibleById(dto.getCategoryId(), userId);

        var previousCategoryTypeName = transaction.getCategory().getCategoryType().getCategoryTypeName();
        var newCategoryTypeName = newCategory.getCategoryType().getCategoryTypeName();
        if ("未分類".equals(previousCategoryTypeName) && !"未分類".equals(newCategoryTypeName)) {
            categoryMappingService.upsertMapping(userId, transaction.getTransactionName(), newCategory);
        }

        transaction.setAccount(account);
        transaction.setCategory(newCategory);
        transaction.setTransactionType(TransactionType.codeOf(dto.getTransactionType()));
        transaction.setTransactionName(dto.getTransactionName());
        transaction.setAmount(dto.getAmount());
        transaction.setTransactionDateTime(dto.getTransactionDateTime());
        transaction.setDescription(dto.getDescription());

        transactionRepository.save(transaction);
    }

    /**
     * 入出金削除
     *
     * @param id 入出金ID
     * @param userId ユーザーID
     */
    public void deleteTransaction(Long id, String userId) {
        var transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("入出金が見つかりません。入出金ID[%d]", id)));
        transactionRepository.delete(transaction);
    }
}
