package com.cydeo.service;

import com.cydeo.dto.CompanyDto;

import java.util.List;

public interface CompanyService {

    CompanyDto findById(Long id);

    List<CompanyDto> listAllCompany();

    CompanyDto getCompanyDtoByLoggedInUser();

    List<CompanyDto> findAllAndSorted();

    void save(CompanyDto companyDto);

    void update(CompanyDto companyDto);

    CompanyDto getUserCompany();

}
