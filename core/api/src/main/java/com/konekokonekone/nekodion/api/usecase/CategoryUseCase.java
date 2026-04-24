package com.konekokonekone.nekodion.api.usecase;

import com.konekokonekone.nekodion.api.response.CategoryTypeResponse;
import com.konekokonekone.nekodion.category.entity.Category;
import com.konekokonekone.nekodion.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryUseCase {

    private final CategoryService categoryService;

    public List<CategoryTypeResponse> getCategories(String userId) {
        var categories = categoryService.findAccessibleCategories(userId);

        // カテゴリー種別でグルーピング（順序を保持）
        var grouped = categories.stream()
                .collect(Collectors.groupingBy(
                        Category::getCategoryType,
                        LinkedHashMap::new, // 順序を保持
                        Collectors.toList()
                ));

        return grouped.entrySet().stream()
                .map(e -> {
                    var type = e.getKey();
                    // カテゴリー種別に属するカテゴリーをレスポンス用に変換
                    var items = e.getValue().stream()
                            .map(c -> CategoryTypeResponse.CategoryItem.builder()
                                    .categoryId(c.getId())
                                    .categoryName(c.getCategoryName())
                                    .build())
                            .toList();
                    return CategoryTypeResponse.builder()
                            .categoryTypeId(type.getId())
                            .categoryTypeName(type.getCategoryTypeName())
                            .isIncome(type.getIsIncome())
                            .categories(items)
                            .build();
                })
                .toList();
    }
}
