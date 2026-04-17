package com.konekokonekone.nekodion.transaction.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TransactionType {
    INCOME("INCOME", "収入", 1),
    EXPENSE("EXPENSE", "支出", 2),
    TRANSFER_OUT("TRANSFER_OUT", "振替（出金）", 3),
    TRANSFER_IN("TRANSFER_IN", "振替（入金）", 4);

    private final String code;

    private final String transactionTypeName;

    private final Integer sortOrder;

    public static TransactionType codeOf(String code) {
        return code == null
                ? null
                : Arrays.stream(TransactionType.values())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElse(null);
    }
}
