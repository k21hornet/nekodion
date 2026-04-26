package com.konekokonekone.nekodion.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class CategoryTypeSummaryDto {

    private final String categoryTypeName;

    private final boolean isIncome;

    private final BigDecimal totalAmount;
}
