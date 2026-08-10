package com.konekokonekone.nekodion.emailingest.entity;

import com.konekokonekone.nekodion.support.entity.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "email_import_logs")
public class EmailImportLog extends AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "message_id")
    private String messageId;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;
}
