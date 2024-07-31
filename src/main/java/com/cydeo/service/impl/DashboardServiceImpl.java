package com.cydeo.service.impl;


import com.cydeo.client.CurrencyClient;
import com.cydeo.dto.Currency.CurrencyDto;
import com.cydeo.enums.InvoiceType;
import com.cydeo.service.DashboardService;
import com.cydeo.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final InvoiceService invoiceService;
    private final CurrencyClient currencyClient;



    @Override
    public Map<String, BigDecimal> getSummaryNumbers() {
        Map<String, BigDecimal> summaryNumbersMap = new HashMap<>();
        BigDecimal totalCost = invoiceService.countTotal(InvoiceType.PURCHASE);
        BigDecimal totalSales = invoiceService.countTotal(InvoiceType.SALES);
        BigDecimal profitLoss = invoiceService.sumProfitLoss();

        summaryNumbersMap.put("totalCost", totalCost);
        summaryNumbersMap.put("totalSales", totalSales);
        summaryNumbersMap.put("profitLoss", profitLoss);
        return summaryNumbersMap;
    }


    @Override
    public CurrencyDto listUsdExchangeRate() {
        return currencyClient.listExchangeRate().getUsd();
    }


}
