package com.example.module1.repository;

import com.example.module1.entity.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Integer> {
    Optional<CurrencyExchange> findByCurrencyType(String currencyType);
    Optional<CurrencyExchange> findById(Integer id);
}
