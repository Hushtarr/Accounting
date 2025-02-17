package com.cydeo.service;

import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.Company;

import java.util.List;

public interface CompanyService {

    CompanyDto findById(Long id);

    List<CompanyDto> listAllCompany();

    CompanyDto getCompanyDtoByLoggedInUser();

    List<CompanyDto> findAllAndSorted();

    void save(CompanyDto companyDto);

    void update(CompanyDto companyDto);

    CompanyDto getUserCompany();

    void activateCompany(Long id);

    void deactivateCompany(Long id);

    void activateOrDeactivateUsers(Company company, boolean status);

    boolean titleIsExist(String companyTitle);

}
