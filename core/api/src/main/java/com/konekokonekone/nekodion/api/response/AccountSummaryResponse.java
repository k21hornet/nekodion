package com.konekokonekone.nekodion.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummaryResponse {

    private String accountType;

    private List<AccountItem> accounts;

    @Data
    public static class AccountItem {

        private Long accountId;

        private String accountName;

        private BigDecimal totalAmount;
    }
}
