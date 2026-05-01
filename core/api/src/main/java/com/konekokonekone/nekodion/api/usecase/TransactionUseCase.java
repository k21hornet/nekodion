package com.konekokonekone.nekodion.api.usecase;

import com.konekokonekone.nekodion.api.mapper.TransactionDetailResponseMapper;
import com.konekokonekone.nekodion.api.mapper.TransactionItemMapper;
import com.konekokonekone.nekodion.api.mapper.TransactionRequestMapper;
import com.konekokonekone.nekodion.api.request.TransactionRequest;
import com.konekokonekone.nekodion.api.response.DailyTransactionResponse;
import com.konekokonekone.nekodion.api.response.MonthlyCategoryTypeSummaryResponse;
import com.konekokonekone.nekodion.api.response.MonthlySummaryResponse;
import com.konekokonekone.nekodion.api.response.TotalAssetsResponse;
import com.konekokonekone.nekodion.api.response.TransactionDetailResponse;
import com.konekokonekone.nekodion.api.response.UnreadCountResponse;
import com.konekokonekone.nekodion.transaction.entity.Transaction;
import com.konekokonekone.nekodion.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionUseCase {

    private final TransactionService transactionService;

    private final TransactionItemMapper transactionItemMapper;

    private final TransactionDetailResponseMapper transactionDetailResponseMapper;

    private final TransactionRequestMapper transactionRequestMapper;

    /**
     * ユーザーの入出金一覧取得
     *
     * @param userId ユーザーID
     * @return 日付ごとの入出金一覧
     */
    public List<DailyTransactionResponse> getTransactions(String userId) {
        var transactions = transactionService.findByUserId(userId);

        // 取引日Map<取引日, 入出金>
        var grouped = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getTransactionDateTime().toLocalDate()));
        return grouped.entrySet().stream()
                .sorted(Map.Entry.<LocalDate, List<Transaction>>comparingByKey().reversed()) // 降順
                .map(e -> {
                    var date = e.getKey().atStartOfDay();
                    var transactionResponses = e.getValue().stream()
                            .map(transactionItemMapper::toItem)
                            .toList();
                    return DailyTransactionResponse.builder()
                            .transactionDateTime(date)
                            .dailyTransactions(transactionResponses)
                            .build();
                }).toList();
    }

    /**
     * 総資産を取得
     *
     * @param userId ユーザーID
     * @return 総資産レスポンス
     */
    public TotalAssetsResponse getTotalAssets(String userId) {
        var totalAssets = transactionService.getTotalAssets(userId);
        return TotalAssetsResponse.builder().totalAssets(totalAssets).build();
    }

    /**
     * 月次収支を取得
     *
     * @param userId ユーザーID
     * @param year   年
     * @param month  月
     * @return 月次収支レスポンス
     */
    public MonthlySummaryResponse getMonthlySummary(String userId, int year, int month) {
        var dto = transactionService.getMonthlySummary(userId, year, month);
        return MonthlySummaryResponse.builder()
                .year(dto.getYear())
                .month(dto.getMonth())
                .totalIncome(dto.getTotalIncome())
                .totalExpense(dto.getTotalExpense())
                .build();
    }

    /**
     * 月次カテゴリー種別ごとの金額集計を取得
     *
     * @param userId ユーザーID
     * @param year   年
     * @param month  月
     * @return 月次カテゴリー種別集計レスポンスリスト
     */
    public List<MonthlyCategoryTypeSummaryResponse> getMonthlyCategoryTypeSummary(String userId, int year, int month) {
        return transactionService.getMonthlyCategoryTypeSummary(userId, year, month).stream()
                .map(dto -> MonthlyCategoryTypeSummaryResponse.builder()
                        .categoryTypeName(dto.getCategoryTypeName())
                        .isIncome(dto.isIncome())
                        .totalAmount(dto.getTotalAmount())
                        .build())
                .toList();
    }

    /**
     * 入出金詳細取得
     *
     * @param id 入出金ID
     * @param userId ユーザーID
     * @return 入出金詳細
     */
    public TransactionDetailResponse getTransaction(Long id, String userId) {
        var transaction = transactionService.findByIdAndUserId(id, userId);
        return transactionDetailResponseMapper.toResponse(transaction);
    }

    /**
     * 未読の入出金一覧取得
     *
     * @param userId ユーザーID
     * @return 日付ごとの未読入出金一覧
     */
    public List<DailyTransactionResponse> getUnreadTransactions(String userId) {
        var transactions = transactionService.findUnreadByUserId(userId);

        var grouped = transactions.stream()
                .collect(Collectors.groupingBy(t -> t.getTransactionDateTime().toLocalDate()));
        return grouped.entrySet().stream()
                .sorted(Map.Entry.<LocalDate, List<Transaction>>comparingByKey().reversed())
                .map(e -> {
                    var date = e.getKey().atStartOfDay();
                    var transactionResponses = e.getValue().stream()
                            .map(transactionItemMapper::toItem)
                            .toList();
                    return DailyTransactionResponse.builder()
                            .transactionDateTime(date)
                            .dailyTransactions(transactionResponses)
                            .build();
                }).toList();
    }

    /**
     * 未読の入出金件数取得
     *
     * @param userId ユーザーID
     * @return 未読件数レスポンス
     */
    public UnreadCountResponse getUnreadCount(String userId) {
        return UnreadCountResponse.builder()
                .count(transactionService.countUnreadByUserId(userId))
                .build();
    }

    /**
     * 指定IDの入出金を既読にする
     *
     * @param userId ユーザーID
     * @param ids 既読にする入出金IDリスト
     */
    public void markAsRead(String userId, List<Long> ids) {
        transactionService.markAsRead(userId, ids);
    }

    /**
     * 入出金記録
     *
     * @param userId ユーザーID
     * @param request 入出金記録リクエスト
     */
    public void createTransaction(String userId, TransactionRequest request) {
        transactionService.createTransaction(userId, transactionRequestMapper.toDto(request));
    }

    /**
     * 入出金更新
     *
     * @param id 入出金ID
     * @param userId ユーザーID
     * @param request 入出金更新リクエスト
     */
    public void updateTransaction(Long id, String userId, TransactionRequest request) {
        transactionService.updateTransaction(id, userId, transactionRequestMapper.toDto(request));
    }

    /**
     * 入出金削除
     *
     * @param id 入出金ID
     * @param userId ユーザーID
     */
    public void deleteTransaction(Long id, String userId) {
        transactionService.deleteTransaction(id, userId);
    }
}
