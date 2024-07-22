package com.cydeo.converter;

import com.cydeo.dto.CompanyDto;
import com.service.CompanyService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CompanyDTOConverter implements Converter<String, CompanyDto> {
    CompanyService companyService;

    @Override
    public CompanyDto convert(String source) {
        if (source == null || source.equals("")) return null;
        return companyService.findById(Long.parseLong(source));
    }
}
