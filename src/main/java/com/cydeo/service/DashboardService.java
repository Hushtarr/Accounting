package com.cydeo.service;


import java.math.BigDecimal;

public interface DashboardService {

    BigDecimal getTotalCost();
    BigDecimal getTotalSales();
    BigDecimal getTotalProfit_Loss();
}
