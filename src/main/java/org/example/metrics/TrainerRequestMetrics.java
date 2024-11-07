package org.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class TrainerRequestMetrics {
    private final Counter trainerCounter;

    /**
     * Trainer Request Metrics.
     */
    public TrainerRequestMetrics(MeterRegistry meterRegistry) {
        trainerCounter = Counter.builder("trainer_requests")
            .description("Number of requests to trainer controller")
            .register(meterRegistry);
    }

    public void incrementCounter() {
        trainerCounter.increment();
    }

}
