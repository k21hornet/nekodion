package com.konekokonekone.nekodion.support.exception;

public class OAuthStateExpiredException extends RuntimeException {

    public OAuthStateExpiredException(String message) {
        super(message);
    }
}
