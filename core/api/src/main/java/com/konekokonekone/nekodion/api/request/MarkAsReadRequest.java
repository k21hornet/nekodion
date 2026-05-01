package com.konekokonekone.nekodion.api.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class MarkAsReadRequest {

    @NotEmpty
    private List<Long> transactionIds;
}
