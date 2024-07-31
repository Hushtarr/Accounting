package com.cydeo.client;

import com.cydeo.dto.Currency.CurrencyDto;
import com.cydeo.dto.Currency.ExchangeRateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url="https://cdn.jsdelivr.net", name = "Currency-Client")
public interface CurrencyClient {

    @GetMapping("/npm/@fawazahmed0/currency-api@latest/v1/currencies/usd.json")
    ExchangeRateDto listExchangeRate();
}
