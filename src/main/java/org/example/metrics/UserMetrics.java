package org.example.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.example.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserMetrics {

    private final Counter userCreationCounter;
    private double myGaugeValue = 0;

    /** Custom metrics. */
    public UserMetrics(MeterRegistry meterRegistry,
                       UserRepository userRepository) {
        userCreationCounter = Counter.builder("users_created")
                .description("Number of users created")
                .register(meterRegistry);

        userCreationCounter.increment(userRepository.count());

        Gauge.builder("active_users", () -> myGaugeValue)
                .description("Number of active users")
                .register(meterRegistry);
    }

    public void incrementCounter() {
        userCreationCounter.increment();
    }

    public void updateGauge(double value) {
        myGaugeValue = value;
    }
}
