package com.konekokonekone.nekodion.external.gmail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GmailMessage {

    private String id;

    private String subject;

    private String from;

    private String date;

    private String body;
}