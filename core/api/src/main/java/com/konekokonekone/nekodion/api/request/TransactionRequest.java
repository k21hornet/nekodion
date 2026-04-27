package com.konekokonekone.nekodion.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionRequest {

    @NotNull
    private Long accountId;

    @NotNull
    private Long categoryId;

    @NotBlank
    private String transactionType;

    @NotBlank
    private String transactionName;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private LocalDateTime transactionDateTime;

    private String description;

    private Boolean isAggregated;
}
