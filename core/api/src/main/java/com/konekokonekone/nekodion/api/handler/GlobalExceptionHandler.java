package com.konekokonekone.nekodion.api.handler;

import com.konekokonekone.nekodion.api.response.ErrorResponse;
import com.konekokonekone.nekodion.support.exception.EntityExistException;
import com.konekokonekone.nekodion.support.exception.EntityNotFoundException;
import com.konekokonekone.nekodion.support.exception.ExternalApiException;
import com.konekokonekone.nekodion.support.exception.GmailNotAuthorizedException;
import com.konekokonekone.nekodion.support.exception.InvalidOAuthStateException;
import com.konekokonekone.nekodion.support.exception.OAuthStateExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(EntityExistException.class)
    public ResponseEntity<ErrorResponse> handleConflict(EntityExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler({InvalidOAuthStateException.class, OAuthStateExpiredException.class})
    public ResponseEntity<ErrorResponse> handleInvalidOAuthState(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(GmailNotAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleGmailNotAuthorized(GmailNotAuthorizedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(403, "Forbidden", ex.getMessage()));
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ErrorResponse> handleExternalApi(ExternalApiException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(500, "Internal Server Error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(400, "Bad Request", message));
    }
}
