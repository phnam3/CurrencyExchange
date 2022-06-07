package com.example.module1.service.impl;

import com.example.module1.entity.CurrencyExchange;
import com.example.module1.repository.CurrencyExchangeRepository;
import com.example.module1.service.CurrencyExchangeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
    private final CurrencyExchangeRepository repository;

    @Override
    public List<CurrencyExchange> findAllRate(){
        return repository.findAll();
    }
    @Override
    public Double findRate(String currency) {
        CurrencyExchange currencyExchange = repository.findByCurrencyType(currency).orElseThrow(
                () -> new RuntimeException("Currency Not Supported")
        );
        return currencyExchange.getRate();
    }

    @Override
    public Double findRate(Integer id) {
        CurrencyExchange currencyExchange = repository.findById(id).orElseThrow(
                () -> new RuntimeException("Currency Not Supported")
        );
        return currencyExchange.getRate();
    }
}
