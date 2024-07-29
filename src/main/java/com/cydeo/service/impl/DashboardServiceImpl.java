package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceType;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.DashboardService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        return listAllByInvoiceType(InvoiceType.PURCHASE)
                .stream()
                .map(InvoiceProduct::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSales() {
        return listAllByInvoiceType(InvoiceType.SALES)
                .stream()
                .map(InvoiceProduct::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalProfit_Loss() {
        return getTotalSales().subtract(getTotalCost());
    }

    @Override
    public List<InvoiceDto> getTransaction() {
        List<InvoiceProductDto> IPlist=repository.findAll()
                .stream()
                .map(i -> mapperUtil.convert(i,new InvoiceProductDto()))
                .toList();

        List<InvoiceDto>Ilist=new ArrayList<>();
        for (InvoiceProductDto invoice:IPlist){
            int quantity= invoice.getQuantity();
            BigDecimal price = invoice.getPrice();
            BigDecimal total=BigDecimal.valueOf(quantity).multiply(price);
            BigDecimal tax= BigDecimal.valueOf(invoice.getTax());
            invoice.getInvoice().setTotal(total);
            invoice.getInvoice().setTax(tax);
            invoice.getInvoice().setPrice(price);
            Ilist.add(invoice.getInvoice());
        }

            return Ilist.subList(Ilist.size()-3,Ilist.size());
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
}
