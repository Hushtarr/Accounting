package com.cydeo.service;

import com.cydeo.dto.ClientVendorDto;

import java.util.List;

public interface ClientVendorService {
    List<ClientVendorDto> listAllClientVendors();
    ClientVendorDto findById(Long id);

}
