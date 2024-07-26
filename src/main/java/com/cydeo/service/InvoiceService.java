package com.cydeo.service;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.enums.InvoiceType;

import java.util.List;

public interface InvoiceService {

    InvoiceDto save(InvoiceDto invoiceDto);
    InvoiceDto findById(Long id);
    void delete(Long id);
    List<InvoiceDto> listAllByTypeAndCompany(InvoiceType invoiceType);
    InvoiceDto generateInvoiceForCompanyByType(InvoiceType invoiceType);
    void update(InvoiceDto invoiceDto);

}
