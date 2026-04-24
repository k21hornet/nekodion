package com.konekokonekone.nekodion.category.entity;

import com.konekokonekone.nekodion.support.entity.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends AbstractBaseEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_type_id")
    private CategoryType categoryType;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "sort_order")
    private Integer sortOrder;
}
