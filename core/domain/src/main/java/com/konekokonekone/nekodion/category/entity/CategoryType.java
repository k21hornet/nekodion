package com.konekokonekone.nekodion.category.entity;

import com.konekokonekone.nekodion.support.entity.AbstractBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "category_types")
public class CategoryType extends AbstractBaseEntity {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "category_type_name")
    private String categoryTypeName;

    @Column(name = "sort_order")
    private Integer sortOrder;

    @Column(name = "is_income")
    private Boolean isIncome;
}
