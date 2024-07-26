package com.cydeo.service.impl;

import com.cydeo.dto.CategoryDto;
import com.cydeo.entity.Category;
import com.cydeo.repository.CategoryRepository;
import com.cydeo.service.CategoryService;
import com.cydeo.service.CompanyService;
import com.cydeo.service.SecurityService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final MapperUtil mapperUtil;
      private final CompanyService companyService;

    public CategoryServiceImpl(CategoryRepository categoryRepository, MapperUtil mapperUtil, CompanyService companyService) {
        this.categoryRepository = categoryRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
    }

    @Override
    public List<CategoryDto> listAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> mapperUtil.convert(category, new CategoryDto())).toList();
    }

    @Override
    public CategoryDto findById(Long id) {
        Category foundCategory = categoryRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        return mapperUtil.convert(foundCategory, new CategoryDto());
    }

    @Override
    public List<CategoryDto> listCategoryByCompany() {
        Long companyId = companyService.getCompanyDtoByLoggedInUser().getId();
        return categoryRepository.findByCompanyIdOrderByDescriptionAsc(companyId).stream()
                .map(category -> mapperUtil.convert(category, new CategoryDto())).toList();
    }
}
