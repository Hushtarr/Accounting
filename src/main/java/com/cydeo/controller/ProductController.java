package com.cydeo.controller;

import com.cydeo.dto.ProductDto;
import com.cydeo.enums.ProductUnit;
import com.cydeo.service.CategoryService;
import com.cydeo.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }


    @GetMapping("/list")
    public String getProducts(Model model) {
//        model.addAttribute("products", productService.listAllProducts());
        model.addAttribute("products", productService.listProductsByCategoryAndName());

                return "product/product-list";

    }

    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("newProduct", new ProductDto());
        model.addAttribute("categories", categoryService.listCategoryByCompany());
        model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));


        return "product/product-create";
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("newProduct") ProductDto productDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("categories", categoryService.listCategoryByCompany());
            model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
            return "product/product-create";
        }
        if (!productService.isNameUnique(productDto.getCategory().getId(), productDto.getName(), null)){
            bindingResult.rejectValue("name", "error.name", "This name already exists.");
            model.addAttribute("categories", categoryService.listCategoryByCompany());
            model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
            return "product/product-create";
        }
        productService.save(productDto);
        return "redirect:/products/list";
    }

    @GetMapping("/update/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model) {
        ProductDto productDto = productService.findById(id);
        model.addAttribute("product", productDto);
        model.addAttribute("categories", categoryService.listCategoryByCompany());
        model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));

        return "product/product-update";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@Valid @ModelAttribute("product") ProductDto productDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("categories", categoryService.listCategoryByCompany());
            model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
            return "product/product-update";
        }
        if (!productService.isNameUnique(productDto.getCategory().getId(), productDto.getName(), null)){
            bindingResult.rejectValue("name", "error.name", "This name already exists.");
            model.addAttribute("categories", categoryService.listCategoryByCompany());
            model.addAttribute("productUnits", Arrays.asList(ProductUnit.values()));
            return "product/product-update";
        }
        productService.update(productDto);
        return "redirect:/products/list";

    }




}
