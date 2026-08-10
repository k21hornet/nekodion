package com.konekokonekone.nekodion.ingest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class ForwardingConfirmationRequest {

    @NotBlank
    private String token;

    @NotBlank
    private String from;

    private String subject;

    private String text;

    @NotNull
    private Instant receivedAt;
}
