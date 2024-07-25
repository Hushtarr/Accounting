package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/purchaseInvoices")
public class PurchaseInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;



    public PurchaseInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
    }


    @GetMapping("/print/{id}")
    public String printPurchaseInvoice(@PathVariable("id") Long id, Model model){

        model.addAttribute("company", invoiceService.findById(id).getCompany());
        model.addAttribute("invoice", invoiceService.findById(id));
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(id));

        return "/invoice/invoice_print";
    }


    @GetMapping("/delete/{id}")
    public String deleteInvoice(@PathVariable("id") Long id) {
         invoiceService.delete(id);
         return "redirect:/invoice/purchase-invoice-create";
    }

    @GetMapping("/list")
    public String listPurchaseInvoice(Model model){

        model.addAttribute("invoices", invoiceService.listAllByTypeAndCompany(InvoiceType.PURCHASE));

        return "/invoice/purchase-invoice-list";
    }
}


