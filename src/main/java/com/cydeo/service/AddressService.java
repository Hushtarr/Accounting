package com.cydeo.service;

import com.cydeo.dto.AddressDto;
import com.cydeo.entity.Address;

public interface AddressService {
    Address save(AddressDto addressDto);
}
