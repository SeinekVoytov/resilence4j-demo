package com.seinekvoytov.unstableservice.service;

import org.springframework.http.ResponseEntity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@org.springframework.stereotype.Service
public class Service {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private volatile boolean simulateServerError = false;

    {
        scheduler.scheduleAtFixedRate(
                () -> simulateServerError = !simulateServerError,
                0,
                30,
                TimeUnit.SECONDS
        );
    }

    public ResponseEntity<Object> perform() {
        if (simulateServerError) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok().build();
    }
}
