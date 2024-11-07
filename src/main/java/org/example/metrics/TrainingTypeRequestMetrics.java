package org.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class TrainingTypeRequestMetrics {
    private final Counter trainingTypeCounter;

    /**
     * Training Type Request Metrics.
     */
    public TrainingTypeRequestMetrics(MeterRegistry meterRegistry) {
        trainingTypeCounter = Counter.builder("training_type_requests")
            .description("Number of requests to training type controller")
            .register(meterRegistry);
    }

    public void incrementCounter() {
        trainingTypeCounter.increment();
    }

}
