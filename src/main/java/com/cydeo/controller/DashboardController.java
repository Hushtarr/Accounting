package com.cydeo.controller;

import com.cydeo.enums.InvoiceStatus;
import com.cydeo.service.DashboardService;
import com.cydeo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final InvoiceService invoiceService;


    @GetMapping
    public String dashboard(Model model){



        model.addAttribute("summaryNumbers", dashboardService.getSummaryNumbers());
        model.addAttribute("invoices", invoiceService.listTop3Approved(InvoiceStatus.APPROVED));
        model.addAttribute("exchangeRates", dashboardService.listUsdExchangeRate());

        return "/dashboard";

    }
}
