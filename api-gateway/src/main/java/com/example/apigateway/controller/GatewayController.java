package com.example.apigateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class GatewayController {
    @RequestMapping("/fallback")
    public Mono<String> fallback(){
        return Mono.just("API is taking too long. Please try again");
    }
}
