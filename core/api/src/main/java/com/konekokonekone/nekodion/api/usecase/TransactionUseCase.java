package com.konekokonekone.nekodion.api.usecase;

import com.konekokonekone.nekodion.api.mapper.TransactionDetailResponseMapper;
import com.konekokonekone.nekodion.api.mapper.TransactionItemMapper;
import com.konekokonekone.nekodion.api.response.DailyTransactionResponse;
import com.konekokonekone.nekodion.api.response.TotalAssetsResponse;
import com.konekokonekone.nekodion.api.response.TransactionDetailResponse;
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
                .collect(Collectors.groupingBy(Transaction::getTransactionDate));
        return grouped.entrySet().stream()
                .sorted(Map.Entry.<LocalDate, List<Transaction>>comparingByKey().reversed()) // 降順
                .map(e -> {
                    var date = e.getKey();
                    var transactionResponses = e.getValue().stream()
                            .map(transactionItemMapper::toItem)
                            .toList();
                    return DailyTransactionResponse.builder()
                            .transactionDate(date)
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
}
