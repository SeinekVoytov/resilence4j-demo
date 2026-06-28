package com.seinekvoytov.circuitbreakerservice.service;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

@org.springframework.stereotype.Service
public class Service {

    private final CircuitBreaker circuitBreaker;
    private final RestClient restClient;

    public Service(final CircuitBreaker circuitBreaker, final RestClient restClient) {
        this.circuitBreaker = circuitBreaker;
        this.restClient = restClient;
    }

    public ResponseEntity<Object> testCircuitBreaker() {
        final var runnable = circuitBreaker.decorateRunnable(
                () -> restClient.post()
                        .uri("/perform")
                        .retrieve()
                        .toBodilessEntity()
        );

        try {
            runnable.run();
            return ResponseEntity.ok().build();
        } catch (CallNotPermittedException e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
