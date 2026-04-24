package com.konekokonekone.nekodion.category.repository;

import com.konekokonekone.nekodion.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
            SELECT
                c
            FROM
                Category c
            JOIN FETCH
                c.categoryType
            WHERE
                (c.userId IS NULL OR c.userId = :userId)
            ORDER BY
                c.categoryType.sortOrder ASC, c.sortOrder ASC
            """)
    List<Category> findAccessibleCategories(String userId);

    @Query("""
            SELECT
                c
            FROM
                Category c
            WHERE
                c.id = :id
                AND (c.userId IS NULL OR c.userId = :userId)
            """)
    Optional<Category> findAccessibleById(Long id, String userId);

    @Query("""
            SELECT
                c
            FROM
                Category c
            JOIN FETCH
                c.categoryType ct
            WHERE ct.categoryTypeName = '未分類'
                AND ct.isIncome = :isIncome
                AND c.userId IS NULL
            """)
    Optional<Category> findUnclassified(boolean isIncome);
}
