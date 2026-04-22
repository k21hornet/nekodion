package com.konekokonekone.nekodion.external.gmail.mapper;

import com.konekokonekone.nekodion.external.gmail.entity.GmailImportLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface GmailImportLogMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    GmailImportLog toEntity(String userId, Long accountId, String gmailMessageId,
                            LocalDateTime transactionDateTime, BigDecimal amount, String shopName);
}
