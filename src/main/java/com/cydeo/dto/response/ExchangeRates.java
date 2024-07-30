package com.cydeo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRates {
    private Double euro;
    private Double japaneseYen;
    private Double canadianDollar;
    private Double indianRupee;
    private Double britishPound;
}