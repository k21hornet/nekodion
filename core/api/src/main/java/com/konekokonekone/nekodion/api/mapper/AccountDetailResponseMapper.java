package com.konekokonekone.nekodion.api.mapper;

import com.konekokonekone.nekodion.api.response.AccountDetailResponse;
import com.konekokonekone.nekodion.transaction.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface AccountDetailResponseMapper {

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "accountType", expression = "java(account.getAccountType().getCode())")
    @Mapping(target = "accountTemplateId", expression = "java(account.getAccountTemplate() != null ? account.getAccountTemplate().getId() : null)")
    @Mapping(target = "totalAmount", expression = "java(getTotalAmount(account))")
    AccountDetailResponse toResponse(Account account);

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
