package com.example.module2.controller;

import com.example.module2.dto.ExchangeRequestInfo;
import com.example.module2.service.CurrencyCalculation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "api/v1/calculate")
@AllArgsConstructor
public class CurrencyCalculationController {

    private final CurrencyCalculation currencyCalculation;

    @PostMapping("/exchange")
    public Double getExchangePrice(@RequestBody ExchangeRequestInfo exchangeRequestInfo){
        return currencyCalculation.calculate(exchangeRequestInfo.getAmount(),exchangeRequestInfo.getRate());
    }
}
