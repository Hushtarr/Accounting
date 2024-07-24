package com.cydeo.service;

import com.cydeo.dto.InvoiceProductDto;

import java.util.List;

public interface InvoiceProductService {

    List<InvoiceProductDto> listAllInvoiceProducts();

    InvoiceProductDto findById(Long id);

    List<InvoiceProductDto> listAllByInvoiceId(Long id);

}
