package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;

    public SalesInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
    }


    @GetMapping("/create")
    public String createSalesInvoice(Model model){

        model.addAttribute("newSalesInvoice", invoiceService.generateInvoiceForCompanyByType(InvoiceType.SALES));
        model.addAttribute("clients", clientVendorService.listAllClientVendorsByType(ClientVendorType.CLIENT));

        return "/invoice/sales-invoice-create";
    }

    @PostMapping("/create")
    public String insertSalesInvoice(@ModelAttribute("newSalesInvoice")InvoiceDto invoiceDto){

        invoiceService.save(invoiceDto);

        //return "redirect:/salesInvoices/update/{invoiceId}";
        return "";
    }

    @GetMapping("/print/{id}")
    public String printSalesInvoice(@PathVariable("id") Long id, Model model){

        model.addAttribute("company", invoiceService.findById(id).getCompany());
        model.addAttribute("invoice", invoiceService.findById(id));
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(id));

        return "/invoice/invoice_print";
    }

    @GetMapping("/list")
    public String listSalesInvoice(Model model){

        model.addAttribute("invoices", invoiceService.listAllByTypeAndCompany(InvoiceType.SALES));

        return "/invoice/sales-invoice-list";
    }
}
