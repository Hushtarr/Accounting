package com.cydeo.controller;

import com.cydeo.dto.CompanyDto;
import com.cydeo.service.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String getAllCompanies(Model model) {
        model.addAttribute("companies", companyService.findAllAndSorted());
        return "/company/company-list";
    }

    @GetMapping("/create")
    public String createCompany(Model model){
        model.addAttribute("newCompany", new CompanyDto());
        return "/company/company-create";
    }

    @PostMapping("/create")
    public String insertCompany(@Valid  @ModelAttribute("newCompany") CompanyDto companyDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
           return "/company/company-create";
        }
        companyService.save(companyDto);
        return "redirect:/companies/list";
    }

    @GetMapping("/update/{id}")
    public String editCompany(@PathVariable("id") Long id, Model model){
        model.addAttribute("company", companyService.findById(id));
        return "/company/company-update";
    }

    @PostMapping("/update/{id}")
    public String updateCompany(@Valid  @ModelAttribute("company") CompanyDto companyDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "/company/company-update";
        }
        companyService.update(companyDto);
        return "redirect:/companies/list";
    }

    @GetMapping("/activate/{id}")
    public String activateCompany(@PathVariable("id") Long id){
         companyService.activateCompany(id);
        return "redirect:/companies/list";
    }

    @GetMapping("/deactivate/{id}")
    public String deactivateCompany(@PathVariable("id") Long id){
        companyService.deactivateCompany(id);
        return "redirect:/companies/list";
    }


}
