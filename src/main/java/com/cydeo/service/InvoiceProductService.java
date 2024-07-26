package com.cydeo.service;

import com.cydeo.dto.InvoiceProductDto;

import java.util.List;

public interface InvoiceProductService {


    InvoiceProductDto findById(Long id);

    List<InvoiceProductDto> listAllByInvoiceId(Long id);

    void save(InvoiceProductDto invoiceProductDto);

    void deleteById(Long id);

}
