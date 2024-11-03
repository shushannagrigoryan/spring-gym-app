package org.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class TrainingRequestMetrics {
    private final Counter trainingCounter;

    /** Training Request Metrics. */
    public TrainingRequestMetrics(MeterRegistry meterRegistry) {
        trainingCounter = Counter.builder("training_requests")
                .description("Number of requests to training controller")
                .register(meterRegistry);
    }

    public void incrementCounter() {
        trainingCounter.increment();
    }

}
