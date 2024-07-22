package com.cydeo.dto;

import com.cydeo.enums.CompanyStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
@Getter
@Setter
@ToString
@NoArgsConstructor
public class CompanyDto {

    private Long id;
    private String title;
    private String phone;
    private String website;

    private AddressDto addressDto;

    private CompanyStatus companyStatus;


}
