package com.konekokonekone.nekodion.api.mapper;

import com.konekokonekone.nekodion.api.response.AccountSummaryResponse.AccountItem;
import com.konekokonekone.nekodion.transaction.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface AccountSummaryResponseMapper {

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "totalAmount", expression = "java(getTotalAmount(account))")
    AccountItem toResponse(Account account);

    default BigDecimal getTotalAmount(Account account) {
        return account.getTransactions().stream()
                .map(t -> switch (t.getTransactionType()) {
                    case INCOME -> t.getAmount();
                    case EXPENSE -> t.getAmount().negate();
                    default -> BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
