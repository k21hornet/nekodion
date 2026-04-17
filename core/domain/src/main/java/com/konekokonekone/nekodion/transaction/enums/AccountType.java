package com.konekokonekone.nekodion.transaction.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum AccountType {
    BANK("BANK", "銀行", 1),
    CARD("CARD", "カード", 2),
    CASH("CASH", "財布（現金管理）", 3),
    OTHER("OTHER", "その他", 4);

    private final String code;

    private final String accountTypeName;

    private final Integer sortOrder;

    public static AccountType codeOf(String code) {
        return code == null
                ? null
                : Arrays.stream(AccountType.values())
                .filter(e -> e.code.equals(code))
                .findFirst()
                .orElse(null);
    }
}
