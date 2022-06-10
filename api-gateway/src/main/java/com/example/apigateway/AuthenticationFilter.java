package com.example.apigateway;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Predicate;

@Component
public class AuthenticationFilter implements GatewayFilter {
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
            if(!confirmRequest(request)){
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        }

        return chain.filter(exchange);
    }

    private boolean confirmRequest(ServerHttpRequest request) {

        String token = request.getHeaders().getOrEmpty("Authorization").get(0);

//        WebClient webClient = WebClient.builder()
//                .baseUrl("http://localhost:8080")
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
//                .defaultHeader("Authentication", token)
//                .build();
//        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = webClient.method(HttpMethod.POST);
//        WebClient.RequestBodySpec bodySpec = uriSpec.uri("/api/v1/auth/confirm");
//        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.body(BodyInserters.fromValue("data"));
//        headersSpec.exchangeToMono(response -> {
//            if (response.statusCode().equals(HttpStatus.OK)) {
//                return response.bodyToMono(String.class);
//            } else if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
//                return Mono.just(response.statusCode().toString());
//            } else {
//                return response.createException().flatMap(Mono::error);
//            }
//        });
        HttpHeaders header = new HttpHeaders() {{
            set("Authorization", token);
        }};
        RestTemplate restTemplate = new RestTemplate();
        try{
            ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/api/v1/auth/confirm", HttpMethod.POST, new HttpEntity<String>(header), String.class);
        } catch (Exception e){
            return false;
        }
//        ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/api/v1/auth/confirm", HttpMethod.GET, new HttpEntity<String>(header), String.class);
//        if(response.getStatusCode().is2xxSuccessful()) {
//            return true;
//        }
        return true;
    }
}
