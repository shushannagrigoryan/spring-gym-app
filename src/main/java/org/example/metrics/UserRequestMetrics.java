package org.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class UserRequestMetrics {
    private final Counter userCounter;

    /** User Request Metrics. */
    public UserRequestMetrics(MeterRegistry meterRegistry) {
        userCounter = Counter.builder("user_requests")
                .description("Number of requests to user controller")
                .register(meterRegistry);
    }

    public void incrementCounter() {
        userCounter.increment();
    }

}
