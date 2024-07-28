package com.cydeo.service.impl;


import com.cydeo.dto.ProductDto;
import com.cydeo.entity.Category;
import com.cydeo.entity.Product;
import com.cydeo.repository.ProductRepository;
import com.cydeo.service.CompanyService;
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
    private final CompanyService companyService;

    public ProductServiceImpl(ProductRepository productRepository, MapperUtil mapperUtil, CompanyService companyService) {
        this.productRepository = productRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
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
    public List<ProductDto> listProductsByCategoryAndName() {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        List<Product> sortedProducts = productRepository.findByCompanyIdOrderByCategoryDescriptionAndProductNameAsc(companyId);
        return sortedProducts.stream()
                .map(product -> mapperUtil.convert(product, new ProductDto()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(ProductDto productDto) {

        Product product = mapperUtil.convert(productDto,new Product());
        product.setCategory(mapperUtil.convert(productDto.getCategory(), new Category()));
        productRepository.save(product);
    }

    @Override
    public void update(ProductDto productDto) {
        Product product = mapperUtil.convert(productDto,new Product());
        product.setCategory(mapperUtil.convert(productDto.getCategory(), new Category()));
        productRepository.save(product);
    }

    @Override
    public List<ProductDto> listAllProductsByCompanyId(Long id) {
        return null;
    }

    @Override
    public List<ProductDto> findAllInStock() {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        List<Product> productsInStock = productRepository.findByCategory_Company_IdAndQuantityInStockGreaterThan(companyId, 0);
        return productsInStock.stream().map(product -> mapperUtil.convert(product, new ProductDto())).collect(Collectors.toList());
    }

}

