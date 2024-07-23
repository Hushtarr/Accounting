package com.cydeo.entity;

import com.cydeo.entity.common.BaseEntity;
import com.cydeo.enums.ProductUnit;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
@Entity
public class Product extends BaseEntity {

    private String name;
    private int quantityInStock;
    private int lowLimitAlert;

    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
