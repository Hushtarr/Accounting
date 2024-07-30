package com.cydeo.controller;


import com.cydeo.service.CurrencyService;
import com.cydeo.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashbordController {
    private final DashboardService dashboardService;
    private final CurrencyService currencyService;

    public DashbordController(DashboardService dashboardService, CurrencyService currencyService) {
        this.dashboardService = dashboardService;
        this.currencyService = currencyService;
    }

    @GetMapping
    public String dashbord(Model model){

        Map<String, BigDecimal> summaryNumbers = new HashMap<>();
        summaryNumbers.put("totalCost", dashboardService.getTotalCost());
        summaryNumbers.put("totalSales", dashboardService.getTotalSales());
        summaryNumbers.put("profitLoss", dashboardService.getTotalProfit_Loss());

        model.addAttribute("summaryNumbers", summaryNumbers);

        model.addAttribute("exchangeRates", currencyService.getExchangeRates());

        return "/dashboard";

    }


}
