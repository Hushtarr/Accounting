package com.cydeo.service.impl;

import com.cydeo.converter.InvoiceDTOConverter;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceType;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.SecurityService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;



    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, MapperUtil mapperUtil, SecurityService securityService) {
        this.repository = invoiceRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
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

    @Override
    public List<InvoiceDto> listSalesInvoicesByCompany() {

        UserDto userDto = securityService.getLoggedInUser();
        String companyTitle = userDto.getCompany().getTitle();

        return repository.findByInvoiceTypeAndCompany_TitleOrderByInvoiceNoDesc(InvoiceType.SALES, companyTitle)
                .stream()
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDto()))
                .toList();
    }

    @Override
    public void update(InvoiceDto invoice) {
        Invoice invoice1 = repository.findByInvoiceType(invoice.getInvoiceType());
        Invoice convertedInvoice = mapperUtil.convert(invoice, new Invoice());
        convertedInvoice.setId(invoice1.getId());
        repository.save(convertedInvoice);

    }
}
