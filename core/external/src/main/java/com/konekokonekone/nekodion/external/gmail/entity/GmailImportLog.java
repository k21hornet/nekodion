package com.konekokonekone.nekodion.external.gmail.entity;

import com.konekokonekone.nekodion.support.entity.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "gmail_import_logs")
public class GmailImportLog extends AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "gmail_message_id")
    private String gmailMessageId;

    @Column(name = "transaction_at")
    private LocalDateTime transactionDateTime;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "shop_name")
    private String shopName;
}
