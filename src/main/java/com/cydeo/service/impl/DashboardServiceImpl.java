package com.cydeo.service.impl;


import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceType;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.DashboardService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final InvoiceProductRepository repository;
    private final CompanyService companyService;
    private final MapperUtil mapperUtil;

    public DashboardServiceImpl(CompanyService companyService, InvoiceProductRepository repository, MapperUtil mapperUtil) {
        this.companyService = companyService;
        this.repository = repository;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public BigDecimal getTotalCost() {
        return listAllByInvoice()
                .stream()
                .map(InvoiceProduct::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSales() {
        return listAllByInvoice()
                .stream()
                .map(i->i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalProfit_Loss() {
        return getTotalSales().subtract(getTotalCost());
    }

    public List<InvoiceProduct> listAllByInvoiceType (InvoiceType invoiceType){
        return repository.findAll()
                .stream()
                .filter(ip ->
                        ip.getInvoice().getCompany().getTitle().equals(companyService.getCompanyDtoByLoggedInUser().getTitle())
                        && ip.getInvoice().getInvoiceType().equals(invoiceType))
                .map(ip->mapperUtil.convert(ip, new InvoiceProduct()))
                .collect(Collectors.toList())
                ;

    }

    public List<InvoiceProduct> listAllByInvoice (){
        return repository.findAll()
                .stream()
                .filter(ip ->
                        ip.getInvoice().getCompany().getTitle().equals(companyService.getCompanyDtoByLoggedInUser().getTitle()))
                .map(ip->mapperUtil.convert(ip, new InvoiceProduct()))
                .collect(Collectors.toList())
                ;

    }
}
