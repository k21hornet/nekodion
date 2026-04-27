package com.konekokonekone.nekodion.api.controller;

import com.konekokonekone.nekodion.api.response.GmailAuthUrlResponse;
import com.konekokonekone.nekodion.api.response.GmailStatusResponse;
import com.konekokonekone.nekodion.api.security.CurrentUser;
import com.konekokonekone.nekodion.api.usecase.GmailAuthUseCase;
import com.konekokonekone.nekodion.support.exception.ExternalApiException;
import com.konekokonekone.nekodion.support.exception.InvalidOAuthStateException;
import com.konekokonekone.nekodion.support.exception.OAuthStateExpiredException;
import com.konekokonekone.nekodion.user.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/gmail")
@RequiredArgsConstructor
public class GmailAuthController {

    private final GmailAuthUseCase gmailAuthUseCase;

    @Value("${gmail.frontend-url}")
    private String frontendUrl;

    @GetMapping("/auth-url")
    public ResponseEntity<GmailAuthUrlResponse> getAuthUrl(@CurrentUser UserDto currentUser) {
        return ResponseEntity.ok(gmailAuthUseCase.getAuthUrl(currentUser.getId()));
    }

    @GetMapping("/callback")
    public void callback(
            @RequestParam String code,
            @RequestParam String state,
            HttpServletResponse response
    ) throws IOException {
        try {
            gmailAuthUseCase.handleCallback(code, state);
            response.sendRedirect(frontendUrl + "/settings");
        } catch (InvalidOAuthStateException | OAuthStateExpiredException | ExternalApiException e) {
            response.sendRedirect(frontendUrl + "/settings?error=gmail_auth_failed");
        }
    }

    @GetMapping("/status")
    public ResponseEntity<GmailStatusResponse> getStatus(@CurrentUser UserDto currentUser) {
        return ResponseEntity.ok(gmailAuthUseCase.getStatus(currentUser.getId()));
    }
}
