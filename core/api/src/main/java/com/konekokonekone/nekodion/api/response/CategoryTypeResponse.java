package com.konekokonekone.nekodion.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTypeResponse {

    private Long categoryTypeId;

    private String categoryTypeName;

    private Boolean isIncome;

    private List<CategoryItem> categories;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryItem {

        private Long categoryId;

        private String categoryName;
    }
}
