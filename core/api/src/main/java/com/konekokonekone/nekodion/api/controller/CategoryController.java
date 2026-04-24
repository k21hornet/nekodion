package com.konekokonekone.nekodion.api.controller;

import com.konekokonekone.nekodion.api.response.CategoryTypeResponse;
import com.konekokonekone.nekodion.api.usecase.CategoryUseCase;
import com.konekokonekone.nekodion.api.security.CurrentUser;
import com.konekokonekone.nekodion.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryUseCase categoryUseCase;

    @GetMapping
    public ResponseEntity<List<CategoryTypeResponse>> getCategories(
            @CurrentUser UserDto currentUser) {
        return ResponseEntity.ok(categoryUseCase.getCategories(currentUser.getId()));
    }
}
