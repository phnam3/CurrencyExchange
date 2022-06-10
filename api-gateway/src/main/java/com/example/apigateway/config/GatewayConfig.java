package com.example.apigateway.config;

import com.example.apigateway.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private final AuthenticationFilter filter;

    public GatewayConfig(AuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("currency_exchange", r -> r.path("/api/v1/exchange/**")
                        .filters(f -> f.filter(filter))
                        .uri(("lb://CURRENCY-RATE-SERVICE")))
                .route("calculate", r -> r.path("/api/v1/calculate/**")
                        .filters(f -> f.filter(filter))
                        .uri(("lb://CURRENCY-CALCULATION-SERVICE")))
                .route("authentication", r -> r.path("/api/v1/auth/**")
                        .uri(("lb://AUTHENTICATION-SERVICE")))
                .build();
    }
}
