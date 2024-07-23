package com.cydeo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private Long id;
    private String description;
    private CompanyDto company;
    private boolean hasProduct;
}
