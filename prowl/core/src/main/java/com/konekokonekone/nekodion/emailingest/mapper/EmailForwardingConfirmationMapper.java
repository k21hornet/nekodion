package com.konekokonekone.nekodion.emailingest.mapper;

import com.konekokonekone.nekodion.emailingest.dto.EmailForwardingConfirmationDto;
import com.konekokonekone.nekodion.emailingest.entity.EmailForwardingConfirmation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmailForwardingConfirmationMapper {

    EmailForwardingConfirmationDto toDto(EmailForwardingConfirmation entity);
}
