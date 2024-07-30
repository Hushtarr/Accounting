package com.cydeo.dto.response;

import lombok.*;

import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CurrencyDto {

    private String date;
    private Map<String, Double> usd = new HashMap<>();
}
