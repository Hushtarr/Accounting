package com.cydeo.controller;

import com.cydeo.dto.CategoryDto;
import com.cydeo.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String getAllCategories(Model model){
//        model.addAttribute("categories", categoryService.listAllCategories());
        model.addAttribute("categories", categoryService.listCategoryByCompany());

        return "category/category-list";
    }

    @GetMapping("/create")
    public String createCategory(Model model){
        model.addAttribute("newCategory", new CategoryDto());
        return "category/category-create";
    }
    @PostMapping("/create")
    public String saveCategory(@ModelAttribute("newCategory") CategoryDto categoryDto){

        categoryService.save(categoryDto);
        return "redirect:/categories/list";
    }
    @GetMapping("/update/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model){
        CategoryDto dto = categoryService.findById(id);
        model.addAttribute("category", dto);
        return "category/category-update";
    }
    @PostMapping("/update/{id}")
    public String updateCategory(@ModelAttribute("category") CategoryDto categoryDto){

        categoryService.update(categoryDto);
        return "redirect:/categories/list";
    }

}
