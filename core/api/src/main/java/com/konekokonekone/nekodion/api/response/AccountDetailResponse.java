package com.konekokonekone.nekodion.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDetailResponse {

    private Long accountId;

    private String accountName;

    private String accountType;

    private Long accountTemplateId;

    private BigDecimal totalAmount;
}
