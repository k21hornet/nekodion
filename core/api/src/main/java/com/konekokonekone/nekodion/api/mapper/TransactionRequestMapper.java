package com.konekokonekone.nekodion.api.mapper;

import com.konekokonekone.nekodion.api.request.TransactionRequest;
import com.konekokonekone.nekodion.transaction.dto.TransactionRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionRequestMapper {

    @Mapping(target = "isRead", ignore = true)
    TransactionRequestDto toDto(TransactionRequest request);
}
