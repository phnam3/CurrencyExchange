package com.example.apigateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
public class GatewayFilter implements org.springframework.cloud.gateway.filter.GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        final List<String> apiEndpoints = List.of("/register", "/login");

        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));

        if (isApiSecured.test(request)) {
            if (!request.getHeaders().containsKey("Authorization")) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            String authorities = confirmRequest(request);

            if ("".equals(authorities)) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            try {
                getAccessList(authorities).contains(request.getURI().getPath());
            } catch (NullPointerException e) {
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.FORBIDDEN);
                return response.setComplete();
            }
        }

        return chain.filter(exchange);
    }

    private String confirmRequest(ServerHttpRequest request) {

        String token = request.getHeaders().getOrEmpty("Authorization").get(0);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = request.getHeaders();

        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/api/v1/auth/confirm",
                HttpMethod.POST, new HttpEntity<String>(token.substring(7), header), String.class);
        if (response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError()) {
            return "";
        }

        return response.getHeaders().getOrEmpty("Authorities").get(0);
    }

    //Manual DB -> should be localized
    private List<String> getAccessList(String authorities) {
        Map<String, List<String>> accessList = new HashMap<>();
        List<String> uriList = new ArrayList<>();
        uriList.add("/api/v1/calculate/exchange");
        accessList.put("SUPER_ADMIN", uriList);

        List<String> result = accessList.get(authorities);
        return result;
    }
}
