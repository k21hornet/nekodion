package com.konekokonekone.nekodion.ingest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class IngestEmailRequest {

    @NotBlank
    private String token;

    @NotBlank
    private String to;

    @NotBlank
    private String from;

    @NotBlank
    private String subject;

    @NotBlank
    private String text;

    private String html;

    private String messageId;

    @NotNull
    private Instant receivedAt;
}
