package com.seinekvoytov.unstableservice.controller;

import com.seinekvoytov.unstableservice.service.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final Service service;

    public Controller(final Service service) {
        this.service = service;
    }

    @PostMapping("/perform")
    public ResponseEntity<Object> randomCircuitBreakerTestEndpoint() {
        System.out.println("Service B: perform");
        return service.perform();
    }
}
