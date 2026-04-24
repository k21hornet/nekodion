package com.konekokonekone.nekodion.category.service;

import com.konekokonekone.nekodion.category.entity.Category;
import com.konekokonekone.nekodion.category.repository.CategoryRepository;
import com.konekokonekone.nekodion.support.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * カテゴリー一覧を取得（ユーザー固有のカテゴリー含む）
     *
     * @param userId ユーザーID
     * @return カテゴリー一覧
     */
    public List<Category> findAccessibleCategories(String userId) {
        return categoryRepository.findAccessibleCategories(userId);
    }
    public Category findAccessibleById(Long id, String userId) {
        return categoryRepository.findAccessibleById(id, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("カテゴリーが見つかりません。カテゴリーID[%d]", id)));
    }
}
