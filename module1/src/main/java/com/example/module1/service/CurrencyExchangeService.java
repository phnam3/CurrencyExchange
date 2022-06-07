package com.example.module1.service;

import com.example.module1.entity.CurrencyExchange;

import java.util.List;

public interface CurrencyExchangeService {
    List<CurrencyExchange> findAllRate();

    Double findRate(String currency);

    Double findRate(Integer id);
}
