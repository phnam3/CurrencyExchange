package com.example.module1.entity;

import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "currency_exchange", schema = "currency")
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class CurrencyExchange {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private Integer id;

    @Basic
    @Column(name = "currency_type")
    private String currencyType;

    @Basic
    @Column(name = "rate")
    private Double rate;
}
