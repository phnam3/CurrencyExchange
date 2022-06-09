package com.example.currencyexchange.repository;

import com.example.currencyexchange.entity.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Integer> {
    Optional<CurrencyExchange> findByCurrencyType(String currencyType);
    Optional<CurrencyExchange> findById(Integer id);
}
