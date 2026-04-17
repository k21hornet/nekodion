package com.konekokonekone.nekodion.transaction.entity.converter;

import com.konekokonekone.nekodion.transaction.enums.TransactionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TransactionTypeConverter implements AttributeConverter<TransactionType, String> {

    @Override
    public String convertToDatabaseColumn(TransactionType attribute) {
        return attribute == null ? null : attribute.getCode();
    }

    @Override
    public TransactionType convertToEntityAttribute(String dbData) {
        return dbData == null ? null : TransactionType.codeOf(dbData);
    }
}
