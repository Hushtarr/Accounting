package com.cydeo.service;

import com.cydeo.dto.InvoiceProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceProductService {


    InvoiceProductDto findById(Long id);

    List<InvoiceProductDto> listAllByInvoiceId(Long id);

    void save(InvoiceProductDto invoiceProductDto);

    void deleteById(Long id);

    BigDecimal getInvoiceProductTotalWithTax(InvoiceProductDto invoiceProductDto);
    BigDecimal getInvoiceProductTotalWithoutTax(InvoiceProductDto invoiceProductDto);

    List<InvoiceProductDto> findAllByInvoiceIdAndCalculateTotalPrice(Long invoiceId);
}
