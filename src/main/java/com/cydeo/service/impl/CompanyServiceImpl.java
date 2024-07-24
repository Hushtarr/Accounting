package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.Company;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.service.SecurityService;
import com.cydeo.util.MapperUtil;
import com.cydeo.service.CompanyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    public CompanyServiceImpl(CompanyRepository companyRepository, MapperUtil mapperUtil, SecurityService securityService) {
        this.companyRepository = companyRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
    }

    @Override
    public CompanyDto findById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow();
        return mapperUtil.convert(company, new CompanyDto());
    }

    @Override
    public List<CompanyDto> listAllCompany() {
        List<Company> companyList = companyRepository.findAll();
        return companyList.stream().map(company -> mapperUtil.convert(company, new CompanyDto())).collect(Collectors.toList());
    }

    @Override
    public CompanyDto getCompanyDtoByLoggedInUser() {
        return securityService.getLoggedInUser().getCompany();
    }


}
