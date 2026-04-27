package com.konekokonekone.nekodion.support.exception;

public class InvalidOAuthStateException extends RuntimeException {

    public InvalidOAuthStateException(String message) {
        super(message);
    }
}
