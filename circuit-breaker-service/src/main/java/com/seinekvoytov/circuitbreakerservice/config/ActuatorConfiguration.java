package com.seinekvoytov.circuitbreakerservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorConfiguration {

    public ActuatorConfiguration(
            final MeterRegistry meterRegistry,
            final CircuitBreaker countBasedCB
    ) {
        Gauge.builder(
                        "count_based_cb_failed_calls",
                        countBasedCB,
                        cb -> cb.getMetrics().getNumberOfFailedCalls()
                )
                .description("The number of failed calls in the current count based circuit breaker window")
                .register(meterRegistry);

        Gauge.builder(
                        "count_based_cb_state",
                        countBasedCB,
                        cb -> cb.getState().ordinal()
                )
                .description("Circuit breaker state: 0=DISABLED, 1=METRICS_ONLY, 2=CLOSED, 3=OPEN, 4=FORCED_OPEN, 5=HALF_OPEN")
                .register(meterRegistry);
    }

    @Bean
    public HealthIndicator countBasedCBHealthIndicator(
            final CircuitBreaker countBasedCB
    ) {
        return () -> Health.up()
                .withDetail("state", countBasedCB.getState())
                .withDetail("failedCalls", countBasedCB.getMetrics().getNumberOfFailedCalls())
                .build();
    }
}
