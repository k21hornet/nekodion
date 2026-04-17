package com.konekokonekone.nekodion.api.mapper;

import com.konekokonekone.nekodion.api.response.TransactionDetailResponse;
import com.konekokonekone.nekodion.transaction.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionDetailResponseMapper {

    @Mapping(target = "accountId", source = "account.id")
    @Mapping(target = "transactionType", source = "transactionType.code")
    @Mapping(target = "description", source = "description")
    TransactionDetailResponse toResponse(Transaction transaction);
}
