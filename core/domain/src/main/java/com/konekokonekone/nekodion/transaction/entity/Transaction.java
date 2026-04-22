package com.konekokonekone.nekodion.transaction.entity;

import com.konekokonekone.nekodion.transaction.entity.converter.TransactionTypeConverter;
import com.konekokonekone.nekodion.transaction.enums.TransactionType;
import com.konekokonekone.nekodion.support.entity.AbstractBaseEntity;
import com.konekokonekone.nekodion.support.util.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction extends AbstractBaseEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "transaction_type")
    @Convert(converter = TransactionTypeConverter.class)
    private TransactionType transactionType;

    @Column(name = "transaction_name")
    private String transactionName;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "transaction_at")
    private LocalDateTime transactionDateTime;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id")
    private Transfer transfer;

    @Column(name = "is_aggregated")
    private Boolean isAggregated;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = IdGenerator.generate();
        }
    }
}
