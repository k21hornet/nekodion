package com.konekokonekone.nekodion.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GmailMessageResponse {

    private String id;

    private String subject;

    private String from;

    private String date;

    private String body;
}
