package com.cydeo.service.impl;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.entity.ClientVendor;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.enums.InvoiceType;
import com.cydeo.repository.InvoiceRepository;
import com.cydeo.service.CompanyService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ProductService;
import com.cydeo.util.MapperUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MapperUtil mapperUtil;
    private final CompanyService companyService;
    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, MapperUtil mapperUtil, CompanyService companyService, InvoiceProductService invoiceProductService, ProductService productService) {
        this.invoiceRepository = invoiceRepository;
        this.mapperUtil = mapperUtil;
        this.companyService = companyService;
        this.invoiceProductService = invoiceProductService;
        this.productService = productService;
    }

    @Override
    public InvoiceDto save(InvoiceDto invoiceDto, InvoiceType invoiceType) {
        invoiceDto.setInvoiceType(invoiceType);
        invoiceDto.setCompany(companyService.getCompanyDtoByLoggedInUser());
        Invoice savedInvoice = invoiceRepository.save(mapperUtil.convert(invoiceDto, new Invoice()));
        return mapperUtil.convert(savedInvoice, new InvoiceDto());
    }

    @Override
    public InvoiceDto findById(Long id) {

        Invoice foundInvoice = invoiceRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Invoice " + id + "not found"));

        return mapperUtil.convert(foundInvoice, new InvoiceDto());
    }

    @Override
    public List<InvoiceDto> listAllByTypeAndCompany(InvoiceType invoiceType) {
        String companyTitle = companyService.getCompanyDtoByLoggedInUser().getTitle();
        return invoiceRepository.findByInvoiceTypeAndCompany_TitleOrderByInvoiceNoDesc(invoiceType, companyTitle)
                .stream()
                .map(invoice -> {
                    InvoiceDto invoiceDto = mapperUtil.convert(invoice, new InvoiceDto());
                    setPriceTaxAndTotal(invoiceDto);
                    return invoiceDto;
                })
                .toList();
    }

    @Override
    public InvoiceDto generateInvoiceForCompanyByType(InvoiceType invoiceType){

        InvoiceDto invoiceDto = new InvoiceDto();

        String prefix;
        int currentInvNum;

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
        if(invoice.get().getInvoiceType().equals(InvoiceType.PURCHASE)){
            List<InvoiceProductDto> invoiceProductDtos = invoiceProductService.listAllByInvoiceId(invoice.get().getId());
            invoiceProductDtos.forEach(ip->{
                invoiceProductService.deleteById(ip.getId());
            });
        }

    }


    @Override
    public void update(InvoiceDto invoiceDto) {
        Invoice invoice = invoiceRepository.findById(invoiceDto.getId()).orElseThrow(IllegalArgumentException::new);
        invoiceDto.setInvoiceStatus(invoice.getInvoiceStatus());
        invoiceDto.setCompany(companyService.getCompanyDtoByLoggedInUser());

        save(invoiceDto, invoice.getInvoiceType());
    }

    @Override
    public List<InvoiceDto> listAllByClientVendor(ClientVendor clientVendor) {
        List<Invoice> invoiceList = invoiceRepository.findByClientVendor(clientVendor);

        return invoiceList.stream().map(invoice -> mapperUtil.convert(invoice, new InvoiceDto())).collect(Collectors.toList());
    }

    @Override
    public void approve(InvoiceDto invoiceDto, InvoiceType invoiceType) {
        invoiceDto.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoiceDto.setDate(LocalDateTime.now());

        if(invoiceDto.getInvoiceType().equals(InvoiceType.PURCHASE)) {
            List<InvoiceProductDto> invoiceProductDtos = invoiceProductService.listAllByInvoiceId(invoiceDto.getId());
            invoiceProductDtos.forEach(i -> {
                ProductDto productDto = i.getProduct();
                productDto.setQuantityInStock(i.getProduct().getQuantityInStock() + i.getQuantity());
                productService.save(productDto);
                mapperUtil.convert(i, new InvoiceProduct());
            });
        }
        save(invoiceDto,invoiceType);

    }

    InvoiceDto setPriceTaxAndTotal(InvoiceDto invoiceDto){
        List<InvoiceProductDto> invoiceProductDtoList = invoiceProductService.listAllByInvoiceId(invoiceDto.getId());
        BigDecimal totalPrice = invoiceProductDtoList.stream().map(invoiceProductService::getInvoiceProductTotalWithoutTax).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal totalWithTax = invoiceProductDtoList.stream().map(invoiceProductService::getInvoiceProductTotalWithTax).reduce(BigDecimal.ZERO,BigDecimal::add);
        BigDecimal totalTax = totalWithTax.subtract(totalPrice);
        invoiceDto.setPrice(totalPrice);
        invoiceDto.setTax(totalTax);
        invoiceDto.setTotal(totalWithTax);
        return invoiceDto;
    }


}
