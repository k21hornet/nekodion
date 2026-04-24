package com.konekokonekone.nekodion.category.repository;

import com.konekokonekone.nekodion.category.entity.CategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryMappingRepository extends JpaRepository<CategoryMapping, Long> {

    @Query("""
            SELECT
                cm
            FROM
                CategoryMapping cm
            JOIN FETCH
                cm.category
            WHERE
                cm.userId = :userId
                AND :transactionName LIKE CONCAT('%', cm.keyword, '%')
            """)
    List<CategoryMapping> findMatchingByUserId(String userId, String transactionName);

    @Query("""
            SELECT
                cm
            FROM
                CategoryMapping cm
            JOIN FETCH
                cm.category
            WHERE
                cm.userId IS NULL
                AND :transactionName LIKE CONCAT('%', cm.keyword, '%')
            """)
    List<CategoryMapping> findMatchingGlobal(String transactionName);

    @Query("""
            SELECT
                cm
            FROM
                CategoryMapping cm
            JOIN FETCH
                cm.category
            WHERE
                cm.userId = :userId
                AND cm.keyword = :keyword
            """)
    Optional<CategoryMapping> findByUserIdAndKeyword(String userId, String keyword);
}
