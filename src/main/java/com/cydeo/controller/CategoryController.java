package com.cydeo.controller;

import com.cydeo.dto.CategoryDto;
import com.cydeo.dto.CompanyDto;
import com.cydeo.service.CategoryService;
import com.cydeo.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CompanyService companyService;

    public CategoryController(CategoryService categoryService, CompanyService companyService) {
        this.categoryService = categoryService;
        this.companyService = companyService;
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
    public String saveCategory(@ModelAttribute("newCategory") @Valid CategoryDto categoryDto, BindingResult bindingResult){
        // we need to ensure the "company" field is properly set with the current user's company before performing any operations that require the company ID. Otherwise we get an error "NullPointerException"
        CompanyDto currentUserCompany = companyService.getCompanyDtoByLoggedInUser();
        categoryDto.setCompany(currentUserCompany);

        if(bindingResult.hasErrors()){
            return "category/category-create";
        }
        if (!categoryService.isDescriptionUnique(categoryDto.getCompany().getId(), categoryDto.getDescription(), null)) {
            bindingResult.rejectValue("description", "error.newCategory", "This description already exists.");
            return "category/category-create";
        }
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
    public String updateCategory(@ModelAttribute("category") @Valid CategoryDto categoryDto, BindingResult bindingResult, Model model){
        // we need to ensure the "company" field is properly set with the current user's company before performing any operations that require the company ID. Otherwise we get an error "NullPointerException"
        CompanyDto currentUserCompany = companyService.getCompanyDtoByLoggedInUser();
        categoryDto.setCompany(currentUserCompany);
        if(bindingResult.hasErrors()){
            return "category/category-update";
        }
        if (!categoryService.isDescriptionUnique(categoryDto.getCompany().getId(), categoryDto.getDescription(), categoryDto.getId())) {
            bindingResult.rejectValue("description", "error.category", "This description already exists.");
            return "category/category-update";
        }
        categoryService.update(categoryDto);
        return "redirect:/categories/list";
    }
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return "redirect:/categories/list";
    }
}
