package com.example.currencyexchange.controller;

import com.example.currencyexchange.dto.CurrencyExchangeInfo;
import com.example.currencyexchange.dto.WebRequestInfo;
import com.example.currencyexchange.entity.CurrencyExchange;
import com.example.currencyexchange.service.CurrencyExchangeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/exchange")
@AllArgsConstructor
@Slf4j
public class CurrencyExchangeController {

    private final CurrencyExchangeService currencyExchangeService;

    @PostMapping("/price")
    public String exchange(@RequestBody CurrencyExchangeInfo currencyExchangeInfo){
        Double rate = currencyExchangeService.findRate(currencyExchangeInfo.getCurrency());
        log.info("Currency: {}, amount: {}, rate: {}",currencyExchangeInfo.getCurrency(),currencyExchangeInfo.getAmount(),rate);
        WebRequestInfo webRequestInfo = new WebRequestInfo(currencyExchangeInfo.getAmount(), rate);
        WebClient webClient = WebClient.builder()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

        //1. define http method
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = webClient.method(HttpMethod.POST);
        //2. define the url from uriSpec
        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/api/v1/calculate/exchange");
        //3. define the request body from bodySpec
        LinkedMultiValueMap map = new LinkedMultiValueMap();
        //TODO: map
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.body(BodyInserters.fromPublisher(
                Mono.just(webRequestInfo), WebRequestInfo.class));
        log.info(headersSpec.toString());
        //4. define the headers from headersSpec
        //TODO: 4.
        //5. getting a response
        Mono<String> specResponse = headersSpec.exchangeToMono( response ->{
            if(response.statusCode().equals(HttpStatus.OK)){
                return response.bodyToMono(String.class);
            } else if(response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()){
                return Mono.just(response.statusCode().toString());
            } else {
                return response.createException().flatMap(Mono::error);
            }
        });
        log.debug("Currency Exchange Price: {}", specResponse.toString());
        return specResponse.block();
    }

    @GetMapping(path = "/rate")
    public List<CurrencyExchange> findAllRate(){
        return currencyExchangeService.findAllRate();
    }

    @GetMapping(path = "/currency")
    public Double findRate(@RequestParam(name = "currency") String currency){
        return currencyExchangeService.findRate(currency);
    }

    @GetMapping(path = "/id")
    public Double findRate(@RequestParam(name = "id") Integer id){
        return currencyExchangeService.findRate(id);
    }
}
