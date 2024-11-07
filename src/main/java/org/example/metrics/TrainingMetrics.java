package org.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.example.repositories.TrainingRepository;
import org.springframework.stereotype.Component;

@Component
public class TrainingMetrics {

    private final Counter trainingCreationCounter;

    /**
     * Custom metrics.
     */
    public TrainingMetrics(MeterRegistry meterRegistry,
                           TrainingRepository trainingRepository) {
        trainingCreationCounter = Counter.builder("trainings_created")
            .description("Number of trainings created")
            .register(meterRegistry);
        trainingCreationCounter.increment(trainingRepository.count());
    }

    public void incrementCounter() {
        trainingCreationCounter.increment();
    }

}
