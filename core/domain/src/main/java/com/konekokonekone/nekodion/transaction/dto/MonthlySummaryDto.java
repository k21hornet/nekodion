package com.konekokonekone.nekodion.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class MonthlySummaryDto {

    private final int year;

    private final int month;

    private final BigDecimal totalIncome;

    private final BigDecimal totalExpense;
}
