package com.konekokonekone.nekodion.api.mapper;

import com.konekokonekone.nekodion.api.response.AccountTemplateResponse;
import com.konekokonekone.nekodion.transaction.entity.AccountTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountTemplateResponseMapper {

    @Mapping(target = "accountType", source = "template.accountType")
    AccountTemplateResponse toResponse(AccountTemplate template);
}
