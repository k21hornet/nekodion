package com.konekokonekone.nekodion.api.controller;

import com.konekokonekone.nekodion.api.response.AccountDetailResponse;
import com.konekokonekone.nekodion.api.response.AccountSummaryResponse;
import com.konekokonekone.nekodion.api.response.AccountTemplateResponse;
import com.konekokonekone.nekodion.api.usecase.AccountUseCase;
import com.konekokonekone.nekodion.api.security.CurrentUser;
import com.konekokonekone.nekodion.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountUseCase accountUseCase;

    @GetMapping
    public ResponseEntity<List<AccountSummaryResponse>> getAccounts(
            @CurrentUser UserDto currentUser) {
        return ResponseEntity.ok(accountUseCase.getAccounts(currentUser.getId()));
    }

    @GetMapping("/templates")
    public ResponseEntity<List<AccountTemplateResponse>> getAccountTemplates() {
        return ResponseEntity.ok(accountUseCase.getAccountTemplates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDetailResponse> getAccount(
            @CurrentUser UserDto currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(accountUseCase.getAccount(id, currentUser.getId()));
    }
}
