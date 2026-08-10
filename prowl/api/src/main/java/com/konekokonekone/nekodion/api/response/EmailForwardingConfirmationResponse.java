package com.konekokonekone.nekodion.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailForwardingConfirmationResponse {

    private String fromAddress;

    private String subject;

    private String bodyText;

    private LocalDateTime receivedAt;
}
