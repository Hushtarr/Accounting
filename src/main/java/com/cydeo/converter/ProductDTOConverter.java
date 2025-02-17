package com.cydeo.converter;

import com.cydeo.dto.ProductDto;
import com.cydeo.service.ProductService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductDTOConverter implements Converter<String, ProductDto> {

    private final ProductService productService;

    public ProductDTOConverter(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ProductDto convert(String source) {
        if (source == null || source.equals("")) return null;
        return productService.findById(Long.parseLong(source));
    }
}
