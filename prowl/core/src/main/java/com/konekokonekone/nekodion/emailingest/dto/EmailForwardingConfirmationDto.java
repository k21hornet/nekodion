package com.konekokonekone.nekodion.emailingest.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EmailForwardingConfirmationDto {

    private String fromAddress;

    private String subject;

    private String bodyText;

    private LocalDateTime receivedAt;
}
