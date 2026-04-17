package com.konekokonekone.nekodion.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyTransactionResponse {

    private LocalDate transactionDate;

    private List<TransactionItem> dailyTransactions;

    @Data
    public static class TransactionItem {

        private Long id;

        private BigDecimal amount;

        private String transactionType;

        private String transactionName;

        private String transactionDescription;
    }
}
