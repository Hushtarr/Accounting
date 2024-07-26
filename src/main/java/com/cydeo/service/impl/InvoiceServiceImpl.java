package com.cydeo.service.impl;

import com.cydeo.dto.CompanyDto;
import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Invoice;
import com.cydeo.enums.InvoiceType;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.SecurityService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;

    private final CompanyService companyService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, MapperUtil mapperUtil, SecurityService securityService, CompanyService companyService) {
        this.invoiceRepository = invoiceRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.companyService = companyService;
    }


    @Override
    public InvoiceDto findById(Long id) {

        Invoice foundInvoice = invoiceRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        return mapperUtil.convert(foundInvoice, new InvoiceDto());
    }

    @Override
    public List<InvoiceDto> listAllByTypeAndCompany(InvoiceType invoiceType) {
        UserDto userDto = securityService.getLoggedInUser();
        String companyTitle = userDto.getCompany().getTitle();
        return invoiceRepository.findByInvoiceTypeAndCompany_TitleOrderByInvoiceNoDesc(invoiceType, companyTitle)
                .stream()
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDto()))
                .toList();
    }

    @Override
    public InvoiceDto generateInvoiceForCompanyByType(InvoiceType invoiceType){

        InvoiceDto invoiceDto = new InvoiceDto();
        invoiceDto.setInvoiceType(invoiceType);
        CompanyDto companyDto = companyService.getCompanyDtoByLoggedInUser();
        invoiceDto.setCompany(companyDto);

        String prefix = "";
        int currentInvNum = 0;

        List<InvoiceDto> invoiceDtoList = listAllByTypeAndCompany(invoiceType);

        if(invoiceType == InvoiceType.SALES) prefix = "S";
        else prefix = "P";

        if(invoiceDtoList.isEmpty()) currentInvNum = 1;
        else {
            String numPart = invoiceDtoList.get(0).getInvoiceNo().substring(2);
            currentInvNum = Integer.parseInt(numPart)+1;
        }

        invoiceDto.setInvoiceNo(String.format("%s-%03d", prefix, currentInvNum));
        invoiceDto.setDate(LocalDateTime.now());
        return invoiceDto;
    }


    @Override
    public void delete(Long id) {

        Optional<Invoice> invoice = invoiceRepository.findById(id);

        if (invoice.isPresent()){
            invoice.get().setIsDeleted(true);
            invoiceRepository.save(invoice.get());
        }

    }

}
