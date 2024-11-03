package org.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class TraineeRequestMetrics {
    private final Counter traineeCounter;

    /** Trainee Request Metrics. */
    public TraineeRequestMetrics(MeterRegistry meterRegistry) {
        traineeCounter = Counter.builder("trainee_requests")
                .description("Number of requests to trainee controller")
                .register(meterRegistry);
    }

    public void incrementCounter() {
        traineeCounter.increment();
    }

}
