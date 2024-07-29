package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDto;
import com.cydeo.entity.Company;
import com.cydeo.entity.User;
import com.cydeo.enums.CompanyStatus;
import com.cydeo.repository.CompanyRepository;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.SecurityService;
import com.cydeo.util.MapperUtil;
import com.cydeo.service.CompanyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;
    private final UserRepository userRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository, MapperUtil mapperUtil, SecurityService securityService, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.userRepository = userRepository;
    }

    @Override
    public CompanyDto findById(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(IllegalArgumentException::new);
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

    @Override
    public List<CompanyDto> findAllAndSorted() {

        return companyRepository.findAllExcludingCompanySortedByStatusAndTitle()
                .stream()
                .map(company -> mapperUtil.convert(company, new CompanyDto()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(CompanyDto companyDto) {
        companyDto.setCompanyStatus(CompanyStatus.PASSIVE);
        Company company = mapperUtil.convert(companyDto, new Company());
        companyRepository.save(company);
    }

    @Override
    public void update(CompanyDto companyDto) {
        Optional<Company> foundCompany = companyRepository.findById(companyDto.getId());
        Company convertedCompany = mapperUtil.convert(companyDto, new Company());
        if (foundCompany.isPresent()) {
            convertedCompany.setId(foundCompany.get().getId());
            convertedCompany.setCompanyStatus(CompanyStatus.ACTIVE);
            companyRepository.save(convertedCompany);
        }

    }

    @Override
    public CompanyDto getUserCompany() {
        return null;
    }

    @Transactional
    @Override
    public void activateCompany(Long id) {
        Company company= companyRepository.findById(id).orElseThrow(()-> new RuntimeException("Company Not found"));
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        companyRepository.save(company);
        activateOrDeactivateUsers(company, true);
    }

    @Transactional
    @Override
    public void deactivateCompany(Long id) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company Not found"));
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        companyRepository.save(company);
        activateOrDeactivateUsers(company, false);
    }

    @Override
    public void activateOrDeactivateUsers(Company company, boolean userStatus) {
        List<User> users = userRepository.findByCompany_Id(company.getId());
        users.forEach(user -> {
            user.setAccountNonLocked(userStatus);
            userRepository.save(user);
        });
    }


}
