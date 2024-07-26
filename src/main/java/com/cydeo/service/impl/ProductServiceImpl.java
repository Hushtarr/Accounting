package com.cydeo.service.impl;

import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Product;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.ProductService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public ProductDto findById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return mapperUtil.convert(product,new ProductDto());

//        Optional<Product> product = productRepository.findById(id);
//        if (product.isPresent()) {
//            return mapperUtil.convert(product.get(), ProductDto.class);}

        }

    @Override
    public List<ProductDto> listAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(product -> mapperUtil.convert(product,new ProductDto())).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> listAllProductsByCompanyId(Long id) {
        List<Product> products = productRepository.findAllByCategory_Company_Id(id);
        return products.stream().map(product -> mapperUtil.convert(product,new ProductDto())).collect(Collectors.toList());
    }


}

