package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.entity.Invoice;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.InvoiceService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;
    private final MapperUtil mapperUtil;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, MapperUtil mapperUtil) {
        this.repository = invoiceRepository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<InvoiceDto> listAllInvoices() {
        return repository.findAll().stream()
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDto()))
                .toList();
    }

    @Override
    public InvoiceDto findById(Long id) {

        Invoice foundInvoice = repository.findById(id).orElseThrow(IllegalArgumentException::new);

        return mapperUtil.convert(foundInvoice, new InvoiceDto());
    }
}
