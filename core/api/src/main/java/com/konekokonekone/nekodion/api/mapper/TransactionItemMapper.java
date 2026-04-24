package com.konekokonekone.nekodion.api.mapper;

import com.konekokonekone.nekodion.api.response.DailyTransactionResponse.TransactionItem;
import com.konekokonekone.nekodion.transaction.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionItemMapper {

    @Mapping(target = "transactionType", source = "transactionType.code")
    @Mapping(target = "transactionDescription", source = "description")
    @Mapping(target = "categoryName", source = "category.categoryName")
    TransactionItem toItem(Transaction transaction);
}
