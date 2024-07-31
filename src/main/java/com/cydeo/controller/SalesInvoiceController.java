package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.enums.ClientVendorType;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.ClientVendorService;
import com.cydeo.service.InvoiceProductService;
import com.cydeo.service.InvoiceService;
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

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final ProductService productService;

    public SalesInvoiceController(InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, ProductService productService, SecurityService securityService) {
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.productService = productService;
    }

    @GetMapping("/create")
    public String createSalesInvoice(Model model) {

        model.addAttribute("newSalesInvoice", invoiceService.generateInvoiceForCompanyByType(InvoiceType.SALES));
        model.addAttribute("clients", clientVendorService.listAllClientVendorsByType(ClientVendorType.CLIENT));

        return "/invoice/sales-invoice-create";
    }

    @PostMapping("/create")
    public String insertSalesInvoice(@ModelAttribute("newSalesInvoice") InvoiceDto invoiceDto) {

        InvoiceDto savedInvoice = invoiceService.save(invoiceDto, InvoiceType.SALES);

        return "redirect:/salesInvoices/update/" + savedInvoice.getId();
    }

    @GetMapping("/print/{id}")
    public String printSalesInvoice(@PathVariable("id") Long id, Model model) {

        model.addAttribute("company", invoiceService.findById(id).getCompany());
        model.addAttribute("invoice", invoiceService.findById(id));
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(id));

        return "/invoice/invoice_print";
    }

    @GetMapping("/list")
    public String listSalesInvoice(Model model) {

        model.addAttribute("invoices", invoiceService.listAllByTypeAndCompany(InvoiceType.SALES));

        return "/invoice/sales-invoice-list";
    }

    @GetMapping("/update/{id}")
    public String editSalesInvoice(@PathVariable("id") Long id, Model model) {

        model.addAttribute("invoice", invoiceService.findById(id));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(id));

        return "/invoice/sales-invoice-update";

    }

    @PostMapping("/addInvoiceProduct/{id}")
    public String addInvoiceProduct(@Valid @ModelAttribute("newInvoiceProduct") InvoiceProductDto invoiceProductDto, BindingResult bindingResult, @PathVariable("id") Long id, Model model) {

        if(bindingResult.hasErrors()) {

            model.addAttribute("invoice", invoiceService.findById(id));
            model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(id));
            return "/invoice/purchase-invoice-update";

        }
      
        invoiceProductDto.setId(null);
        invoiceProductDto.setInvoice(invoiceService.findById(id));
        invoiceProductService.save(invoiceProductDto);
        return "redirect:/salesInvoices/update/" +id;

    }

    @GetMapping("/removeInvoiceProduct/{invoiceId}/{invoiceProuductId}")
    public String removeInvoiceProduct(@PathVariable("invoiceId") Long invoiceId,
                                       @PathVariable("invoiceProuductId") Long invoiceProductId, Model model) {

        invoiceProductService.deleteById(invoiceProductId);

        model.addAttribute("invoice", invoiceService.findById(invoiceId));
        model.addAttribute("newInvoiceProduct", new InvoiceProductDto());
        model.addAttribute("invoiceProducts", invoiceProductService.listAllByInvoiceId(invoiceId));

        return "/invoice/sales-invoice-update";

    }

    @PostMapping("/update/{id}")
    public String updateSalesInvoice(@ModelAttribute("invoice") InvoiceDto invoiceDto,
                                     @PathVariable("id") Long id) {

        invoiceService.update(invoiceDto);

        return "redirect:/salesInvoices/list";

    }

    @GetMapping("/delete/{id}")
    public String deleteSalesInvoice(@PathVariable("id") Long id) {
        invoiceService.delete(id);
        return "redirect:/salesInvoices/list";
    }

    @ModelAttribute
    public void commonAttributes(Model model) {
        model.addAttribute("clients", clientVendorService.listAllClientVendorsByType(ClientVendorType.CLIENT));
        model.addAttribute("products", productService.listProductsByCategoryAndName());
    }

}
