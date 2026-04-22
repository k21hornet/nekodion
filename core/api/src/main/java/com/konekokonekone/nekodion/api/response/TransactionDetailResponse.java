package com.konekokonekone.nekodion.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailResponse {

    private Long id;

    private Long accountId;

    private String transactionType;

    private String transactionName;

    private BigDecimal amount;

    private LocalDateTime transactionDateTime;

    private String description;
}
