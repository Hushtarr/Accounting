package com.cydeo.service;

import com.cydeo.dto.ClientVendorDto;
import com.cydeo.enums.ClientVendorType;

import java.util.List;

public interface ClientVendorService {
    List<ClientVendorDto> listAllClientVendors();
    ClientVendorDto findById(Long id);
    List<ClientVendorDto> listAllClientVendorsByCompany();
    List<ClientVendorDto> listAllClientVendorsByType(ClientVendorType clientVendorType);
    void delete(Long id);
    void save(ClientVendorDto clientVendorDto);
    void update(ClientVendorDto clientVendorDto);
    List<ClientVendorType> findAllTypes();

}
