package com.konekokonekone.nekodion.api.response;

public record ErrorResponse(int status, String error, String message) {
}
