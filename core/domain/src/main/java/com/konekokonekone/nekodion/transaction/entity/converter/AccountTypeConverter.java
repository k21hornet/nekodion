package com.konekokonekone.nekodion.transaction.entity.converter;

import com.konekokonekone.nekodion.transaction.enums.AccountType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AccountTypeConverter implements AttributeConverter<AccountType, String> {

    @Override
    public String convertToDatabaseColumn(AccountType attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public AccountType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AccountType.codeOf(dbData);
    }
}
