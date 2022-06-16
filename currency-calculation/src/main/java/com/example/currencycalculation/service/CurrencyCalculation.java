package com.example.currencycalculation.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrencyCalculation {
    public Double calculate(Integer amount, Double rate) {
//        try {
//            Thread.sleep(5000);
//        } catch (Exception e){}
        return amount * rate;
    }
}
