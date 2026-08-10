package com.konekokonekone.nekodion.api.controller;

import com.konekokonekone.nekodion.api.response.EmailForwardingAddressResponse;
import com.konekokonekone.nekodion.api.response.EmailForwardingConfirmationResponse;
import com.konekokonekone.nekodion.api.security.CurrentUser;
import com.konekokonekone.nekodion.api.usecase.EmailForwardingUseCase;
import com.konekokonekone.nekodion.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email-forwarding")
@RequiredArgsConstructor
public class EmailForwardingController {

    private final EmailForwardingUseCase emailForwardingUseCase;

    @GetMapping("/address")
    public ResponseEntity<EmailForwardingAddressResponse> getAddress(@CurrentUser UserDto currentUser) {
        return ResponseEntity.ok(emailForwardingUseCase.getAddress(currentUser.getId()));
    }

    @GetMapping("/confirmation")
    public ResponseEntity<EmailForwardingConfirmationResponse> getConfirmation(@CurrentUser UserDto currentUser) {
        return emailForwardingUseCase.getConfirmation(currentUser.getId())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
