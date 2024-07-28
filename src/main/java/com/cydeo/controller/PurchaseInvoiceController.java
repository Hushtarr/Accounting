package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.UserDto;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.*;
import com.cydeo.enums.ClientVendorType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/purchaseInvoices")
public class PurchaseInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;
    private final CompanyService companyService;

    public PurchaseInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, ProductService productService, CompanyService companyService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
        this.companyService = companyService;
    }

    @GetMapping("/print/{id}")
    public String printPurchaseInvoice(@PathVariable("id") Long id, Model model) {

        model.addAttribute("company", invoiceService.findById(id).getCompany());
        model.addAttribute("invoice", invoiceService.findById(id));
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(id));

        return "/invoice/invoice_print";
    }


    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable("id") Long id) {
         invoiceService.delete(id);
         return "redirect:/purchaseInvoices/create";
    }

    @GetMapping("/list")
    public String listPurchaseInvoice(Model model){

        model.addAttribute("invoices", invoiceService.listAllByTypeAndCompany(InvoiceType.PURCHASE));

        return "/invoice/purchase-invoice-list";
    }

    @GetMapping("/create")
    public String createPurchaseInvoice(Model model) {
        model.addAttribute("newPurchaseInvoice", invoiceService.generateInvoiceForCompanyByType(InvoiceType.PURCHASE));
        model.addAttribute("vendors", clientVendorService.listAllClientVendorsByType(ClientVendorType.VENDOR));

        return "/invoice/purchase-invoice-create";
    }

    @PostMapping("/create")
    public String insertPurchaseInvoice(@ModelAttribute("newPurchaseInvoice") InvoiceDto newPurchaseInvoice) {
        InvoiceDto savedInvoice = invoiceService.save(newPurchaseInvoice, InvoiceType.PURCHASE);
        return "redirect:/purchaseInvoices/update/"+savedInvoice.getId();
    }

    @GetMapping("/approve/{id}")
    public String approveInvoice(@PathVariable("id") Long id){
        InvoiceDto invoiceDto = invoiceService.findById(id);


        return "/invoice/purchase-invoice-list";

    }


    @ModelAttribute
    public void commonAttributes(Model model) {
        model.addAttribute("Title","Cydeo Accounting-Purchase_Invoice");
    }

}


