package com.cydeo.service;

import com.cydeo.dto.ProductDto;

import java.util.List;


public interface ProductService {

    ProductDto findById(Long id);
    List<ProductDto> listAllProducts();
    List<ProductDto> listProductsByCategoryAndName();
    void save(ProductDto productDto);
    void update(ProductDto productDto);
    void delete(Long id);
    List<ProductDto> listAllProductsByCompanyId(Long id);
}
