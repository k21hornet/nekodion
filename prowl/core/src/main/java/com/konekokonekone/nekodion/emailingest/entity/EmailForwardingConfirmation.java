package com.konekokonekone.nekodion.emailingest.entity;

import com.konekokonekone.nekodion.support.entity.AbstractBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "email_forwarding_confirmations")
public class EmailForwardingConfirmation extends AbstractBaseEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "from_address")
    private String fromAddress;

    @Column(name = "subject")
    private String subject;

    @Column(name = "body_text", columnDefinition = "TEXT")
    private String bodyText;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;
}
