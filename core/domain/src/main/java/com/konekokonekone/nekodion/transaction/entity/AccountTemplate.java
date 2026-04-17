package com.konekokonekone.nekodion.transaction.entity;

import com.konekokonekone.nekodion.transaction.entity.converter.AccountTypeConverter;
import com.konekokonekone.nekodion.transaction.enums.AccountType;
import com.konekokonekone.nekodion.support.entity.AbstractBaseEntity;
import com.konekokonekone.nekodion.support.util.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "account_templates")
public class AccountTemplate extends AbstractBaseEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "account_type")
    @Convert(converter = AccountTypeConverter.class)
    private AccountType accountType;

    @Column(name = "account_name")
    private String accountName;

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = IdGenerator.generate();
        }
    }
}
