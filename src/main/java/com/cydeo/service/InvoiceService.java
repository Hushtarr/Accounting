package com.cydeo.service;

import com.cydeo.dto.InvoiceDto;

import java.util.List;

public interface InvoiceService {

    List<InvoiceDto> listAllInvoices();
    InvoiceDto findById(Long id);
    List<InvoiceDto> listSalesInvoicesByCompany();
    List<InvoiceDto> listPurchaseInvoicesByCompany();

    InvoiceDto save(InvoiceDto invoiceDto);
}
