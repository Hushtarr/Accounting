package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.dto.InvoiceProductDto;
import com.cydeo.dto.ProductDto;
import com.cydeo.dto.UserDto;
import com.cydeo.entity.Product;
import com.cydeo.entity.User;
import com.cydeo.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;
    private final SecurityService securityService;

    public SalesInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, ProductService productService, SecurityService securityService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
        this.securityService = securityService;
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

        model.addAttribute("invoices", invoiceService.listSalesInvoicesByCompany());

        return "/invoice/sales-invoice-list";
    }

    @GetMapping("/update/{id}")
    public String editSalesInvoice(@PathVariable("id") Long id, Model model) {

        UserDto currentUser = securityService.getLoggedInUser();

        model.addAttribute("invoice", invoiceService.findById(id));
        model.addAttribute("clients", clientVendorService.listAllClientVendors());
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("products", productService.listAllProductsByCompanyId(currentUser.getCompany().getId()));
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(id));

        return "/invoice/sales-invoice-update";

    }

    @PostMapping("/addInvoiceProduct/{id}")
    public String addInvoiceProduct(@ModelAttribute("newInvoiceProduct") @Valid InvoiceProductDto invoiceProductDto, BindingResult bindingResult,
                                    @PathVariable("id") Long id, Model model) {

        invoiceProductDto.setInvoice(invoiceService.findById(id));
        UserDto currentUser = securityService.getLoggedInUser();

        if (bindingResult.hasErrors()) {

            model.addAttribute("invoice", invoiceService.findById(id));
            model.addAttribute("clients", clientVendorService.listAllClientVendors());
            model.addAttribute("products", productService.listAllProductsByCompanyId(currentUser.getCompany().getId()));
            model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(id));
            return "/invoice/sales-invoice-update";

        }

        invoiceProductService.save(invoiceProductDto);
        model.addAttribute("invoice", invoiceService.findById(id));
        model.addAttribute("clients", clientVendorService.listAllClientVendors());
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("products", productService.listAllProductsByCompanyId(currentUser.getCompany().getId()));
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(id));

        return "/invoice/sales-invoice-update";

    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProuductId}")
    public String removeInvoiceProduct(@PathVariable("invoiceId") Long invoiceId,
                                       @PathVariable("invoiceProuductId") Long invoiceProductId, Model model) {

        invoiceProductService.deleteById(invoiceProductId);
        UserDto currentUser = securityService.getLoggedInUser();

        model.addAttribute("invoice", invoiceService.findById(invoiceId));
        model.addAttribute("clients", clientVendorService.listAllClientVendors());
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("products", productService.listAllProductsByCompanyId(currentUser.getCompany().getId()));
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(invoiceId));

        return "/invoice/sales-invoice-update";

    }

    @PostMapping("/update/{id}")
    public String updateSalesInvoice( @ModelAttribute("invoice") InvoiceDto invoiceDto,
                                      @PathVariable("id") Long id, Model model) {

        invoiceService.update(invoiceDto);

        return "redirect:/salesInvoices/list";

    }

}
