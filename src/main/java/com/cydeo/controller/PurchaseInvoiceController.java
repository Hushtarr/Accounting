package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.*;
import com.cydeo.enums.ClientVendorType;
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

    public PurchaseInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, ProductService productService, CompanyService companyService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
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
         return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("/list")
    public String listPurchaseInvoice(Model model){

        model.addAttribute("invoices", invoiceService.listAllByTypeAndCompany(InvoiceType.PURCHASE));

        return "/invoice/purchase-invoice-list";
    }

    @GetMapping("/create")
    public String createPurchaseInvoice(Model model) {
        model.addAttribute("newPurchaseInvoice", invoiceService.generateInvoiceForCompanyByType(InvoiceType.PURCHASE));
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
        invoiceService.approve(invoiceDto, InvoiceType.PURCHASE);
        return "redirect:/purchaseInvoices/list";
    }


    @GetMapping("/update/{id}")
    public String editInvoice(@PathVariable("id") Long invoiceId, Model model){


        model.addAttribute("invoice", invoiceService.findById(invoiceId));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(invoiceId));



        return "/invoice/purchase-invoice-update";

    }

    @PostMapping("/update/{id}")
    public String updateInvoice(@Valid @ModelAttribute("invoice") InvoiceDto invoice,
                                BindingResult bindingResult, @PathVariable("id") Long id,
                                Model model){

        if (bindingResult.hasErrors()) {
            return "/invoice/purchase-invoice-update";
        }
        invoiceService.update(invoice);

        return "redirect:/purchaseInvoices/list";


    }

    @PostMapping("/addInvoiceProduct/{id}")
    public String addInvoiceProduct(@Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto invoiceProductDto, BindingResult bindingResult, @PathVariable("id") Long id, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("invoice", invoiceService.findById(id));
            model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(id));
            return "/invoice/purchase-invoice-update";
        }
        invoiceProductDto.setInvoice(invoiceService.findById(id));

        invoiceProductService.save(invoiceProductDto);

        return "redirect:/purchaseInvoices/update/"+id;
    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProuductId}")
    public String removeInvoiceProduct(@PathVariable("invoiceId") Long invoiceid, @PathVariable("invoiceProuductId") Long invoiceProductid){

        invoiceProductService.deleteById(invoiceProductid);

        return "redirect:/purchaseInvoices/update/"+invoiceid;
    }

    @ModelAttribute
    public void commonAttributes(Model model){
        model.addAttribute("products", productService.listProductsByCategoryAndName());
        model.addAttribute("vendors", clientVendorService.listAllClientVendorsByType(ClientVendorType.VENDOR));
        model.addAttribute("title","Cydeo Accounting-Purchase Invoice");
    }

}




