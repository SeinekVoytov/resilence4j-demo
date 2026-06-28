package com.seinekvoytov.circuitbreakerservice.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    CircuitBreakerConfig defaultCBConfig() {
        return CircuitBreakerConfig.custom()
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .minimumNumberOfCalls(5)
                .failureRateThreshold(0.5f)
                .slowCallDurationThreshold(Duration.ofSeconds(60))
                .slowCallRateThreshold(1.0f)
                .waitDurationInOpenState(Duration.ofSeconds(20))
                .permittedNumberOfCallsInHalfOpenState(3)
                .build();
    }

    @Bean
    public CircuitBreakerRegistry cbRegistry(
            final CircuitBreakerConfig cbConfig
    ) {
        return CircuitBreakerRegistry.custom()
                .withCircuitBreakerConfig(cbConfig)
                .build();
    }

    @Bean
    public CircuitBreaker countBasedCircuitBreaker(
            final CircuitBreakerRegistry cbr,
            final @Value("${resilience4j.circuitbreakers.countBasedCBName}") String cbName
    ) {
        return cbr.circuitBreaker(cbName);
    }
}
