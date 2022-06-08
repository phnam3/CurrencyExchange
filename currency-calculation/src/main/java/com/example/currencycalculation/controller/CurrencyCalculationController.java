package com.example.currencycalculation.controller;

import com.example.currencycalculation.dto.ExchangeRequestInfo;
import com.example.currencycalculation.service.CurrencyCalculation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/calculate")
@AllArgsConstructor
@Slf4j
public class CurrencyCalculationController {

    private final CurrencyCalculation currencyCalculation;

    @PostMapping("/exchange")
    public Double getExchangePrice(@RequestBody ExchangeRequestInfo exchangeRequestInfo){
        log.info("Amount: {}  Rate : {}", exchangeRequestInfo.getAmount(), exchangeRequestInfo.getRate());
        return currencyCalculation.calculate(exchangeRequestInfo.getAmount(),exchangeRequestInfo.getRate());
    }
}
