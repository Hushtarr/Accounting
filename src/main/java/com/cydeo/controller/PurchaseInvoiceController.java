package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.entity.Invoice;
import com.cydeo.entity.InvoiceProduct;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
import com.cydeo.service.ProductService;
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


    public PurchaseInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, ProductService productService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
    }

    @GetMapping("/print/{id}")
    public String printPurchaseInvoice(@PathVariable("id") Long id, Model model){

        model.addAttribute("company", invoiceService.findById(id).getCompany());
        model.addAttribute("invoice", invoiceService.findById(id));
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(id));

        return "/invoice/invoice_print";
    }

    @GetMapping("/update/{invoiceId}")
    public String editInvoice(@PathVariable("invoiceId") Long invoiceId, Model model){

        model.addAttribute("invoice", invoiceService.findById(invoiceId));
        model.addAttribute("clientVendor", clientVendorService.listAllClientVendors());
        model.addAttribute("products", productService.findAllInStock());
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());

        return "/invoice/purchase-invoice-update";

    }

    @PostMapping("/update")
    public String updateInvoice(@Valid @ModelAttribute("invoice") InvoiceDto invoice,
                                BindingResult bindingResult, @ModelAttribute("newInvoiceProduct") InvoiceProductDto newProduct,
                                Model model){

        if (bindingResult.hasErrors()) {

            model.addAttribute("products", productService.findAllInStock());
            model.addAttribute("vendors", clientVendorService.listAllClientVendors());
            return "/invoice/purchase-invoice-update";
        }

        invoiceService.update(invoice);

        return "redirect:/invoice/purchase-invoice-create";


    }



}
