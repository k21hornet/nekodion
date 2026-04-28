package com.konekokonekone.nekodion.external.gmail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
public class GmailMessage {

    private String id;

    private String subject;

    private String from;

    private String date;

    private String body;

    private ZonedDateTime sentAt;
}