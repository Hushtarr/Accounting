package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.repository.InvoiceProductRepository;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository repository;
    private final MapperUtil mapperUtil;

    public InvoiceProductServiceImpl(InvoiceProductRepository repository, MapperUtil mapperUtil) {
        this.repository = repository;
        this.mapperUtil = mapperUtil;
    }


    @Override
    public InvoiceProductDto findById(Long id) {

        InvoiceProduct foundInvoiceProduct = repository.findById(id).orElseThrow(IllegalArgumentException::new);

        return mapperUtil.convert(foundInvoiceProduct, new InvoiceProductDto());
    }

    @Override
    public List<InvoiceProductDto> listAllByInvoiceId(Long id) {
        return repository.findAllByInvoiceId(id).stream()
                .map(invoiceProduct -> {
                    InvoiceProductDto invoiceProductDto = mapperUtil.convert(invoiceProduct, new InvoiceProductDto());
                    invoiceProductDto.setTotal(getInvoiceProductTotalWithTax(invoiceProductDto));
                    return invoiceProductDto;
                })
                .toList();
    }

    @Override
    public void save(InvoiceProductDto invoiceProductDto) {
        repository.save(mapperUtil.convert(invoiceProductDto, new InvoiceProduct()));
    }

    @Override
    public void deleteById(Long id) {
        InvoiceProduct invoiceProduct = repository.findById(id).orElseThrow(IllegalArgumentException::new);
        invoiceProduct.setIsDeleted(true);
        repository.save(invoiceProduct);
    }

    @Override
    public BigDecimal getInvoiceProductTotalWithTax(InvoiceProductDto invoiceProductDto) {

        BigDecimal totalWithoutTax = getInvoiceProductTotalWithoutTax(invoiceProductDto);
        BigDecimal tax = (totalWithoutTax.multiply(BigDecimal.valueOf(invoiceProductDto.getTax()))).divide(BigDecimal.valueOf(100), RoundingMode.DOWN);

        return totalWithoutTax.add(tax);
    }

    @Override
    public BigDecimal getInvoiceProductTotalWithoutTax(InvoiceProductDto invoiceProductDto) {

        return invoiceProductDto.getPrice().multiply(BigDecimal.valueOf(invoiceProductDto.getQuantity()));
    }


}
