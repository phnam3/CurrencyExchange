package com.example.currencyexchange.service;

import com.example.currencyexchange.entity.CurrencyExchange;

import java.util.List;

public interface CurrencyExchangeService {
    List<CurrencyExchange> findAllRate();

    Double findRate(String currency);

    Double findRate(Integer id);
}
