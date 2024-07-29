package com.cydeo.service;

import com.cydeo.dto.InvoiceDto;

import java.math.BigDecimal;
import java.util.List;

public interface DashboardService {

    BigDecimal getTotalCost();
    BigDecimal getTotalSales();
    BigDecimal getTotalProfit_Loss();
    List<InvoiceDto>getTransaction();
}
