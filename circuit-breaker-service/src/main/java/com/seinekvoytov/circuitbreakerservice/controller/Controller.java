package com.seinekvoytov.circuitbreakerservice.controller;

import com.seinekvoytov.circuitbreakerservice.service.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final Service service;

    public Controller(final Service service) {
        this.service = service;
    }

    @PostMapping("/testCircuitBreaker")
    public ResponseEntity<Object> testCircuitBreaker() {
        System.out.println("Service A: testCircutBreaker");
        return service.testCircuitBreaker();
    }
}
