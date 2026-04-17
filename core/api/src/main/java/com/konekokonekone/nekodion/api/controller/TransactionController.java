package com.konekokonekone.nekodion.api.controller;

import com.konekokonekone.nekodion.api.response.DailyTransactionResponse;
import com.konekokonekone.nekodion.api.response.TotalAssetsResponse;
import com.konekokonekone.nekodion.api.response.TransactionDetailResponse;
import com.konekokonekone.nekodion.api.usecase.TransactionUseCase;
import com.konekokonekone.nekodion.api.security.CurrentUser;
import com.konekokonekone.nekodion.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionUseCase transactionUseCase;

    @GetMapping
    public ResponseEntity<List<DailyTransactionResponse>> getTransactions(
            @CurrentUser UserDto currentUser) {
        return ResponseEntity.ok(transactionUseCase.getTransactions(currentUser.getId()));
    }

    @GetMapping("/total-assets")
    public ResponseEntity<TotalAssetsResponse> getTotalAssets(
            @CurrentUser UserDto currentUser) {
        return ResponseEntity.ok(transactionUseCase.getTotalAssets(currentUser.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDetailResponse> getTransaction(
            @CurrentUser UserDto currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(transactionUseCase.getTransaction(id, currentUser.getId()));
    }
}
