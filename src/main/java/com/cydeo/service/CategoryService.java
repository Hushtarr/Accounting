package com.cydeo.service;

import com.cydeo.dto.CategoryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<CategoryDto> listAllCategories();
    CategoryDto findById(Long id);
}
