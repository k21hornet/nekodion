package com.konekokonekone.nekodion.api.controller;

import com.konekokonekone.nekodion.api.request.TransactionRequest;
import com.konekokonekone.nekodion.api.response.DailyTransactionResponse;
import com.konekokonekone.nekodion.api.response.MonthlySummaryResponse;
import com.konekokonekone.nekodion.api.response.TotalAssetsResponse;
import com.konekokonekone.nekodion.api.response.TransactionDetailResponse;
import com.konekokonekone.nekodion.api.usecase.TransactionUseCase;
import com.konekokonekone.nekodion.api.security.CurrentUser;
import com.konekokonekone.nekodion.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    @GetMapping("/monthly-summary")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(
            @CurrentUser UserDto currentUser,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(transactionUseCase.getMonthlySummary(currentUser.getId(), year, month));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDetailResponse> getTransaction(
            @CurrentUser UserDto currentUser,
            @PathVariable Long id) {
        return ResponseEntity.ok(transactionUseCase.getTransaction(id, currentUser.getId()));
    }

    @PostMapping
    public ResponseEntity<Void> createTransaction(
            @CurrentUser UserDto currentUser,
            @RequestBody @Validated TransactionRequest request) {
        transactionUseCase.createTransaction(currentUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTransaction(
            @CurrentUser UserDto currentUser,
            @PathVariable Long id,
            @RequestBody @Validated TransactionRequest request) {
        transactionUseCase.updateTransaction(id, currentUser.getId(), request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @CurrentUser UserDto currentUser,
            @PathVariable Long id) {
        transactionUseCase.deleteTransaction(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}
