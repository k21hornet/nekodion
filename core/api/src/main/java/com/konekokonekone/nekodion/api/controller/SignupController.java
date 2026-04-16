package com.konekokonekone.nekodion.api.controller;

import com.konekokonekone.nekodion.api.usecase.SignupUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class SignupController {

    private final SignupUseCase signupUseCase;

    @PostMapping("/signup")
    public ResponseEntity<Void> syncUser(@AuthenticationPrincipal Jwt jwt) {
        signupUseCase.syncUser(jwt);
        return ResponseEntity.status(201).build();
    }
}
