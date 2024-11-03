package org.example.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

@Component
public class MemoryHealthIndicator implements HealthIndicator {

    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    @Override
    public Health health() {
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();

        long maxHeapMemory = heapMemoryUsage.getMax();
        long usedHeapMemory = heapMemoryUsage.getUsed();
        long maxNonHeapMemory = nonHeapMemoryUsage.getMax();
        long usedNonHeapMemory = nonHeapMemoryUsage.getUsed();

        if ((double) usedHeapMemory / maxHeapMemory * 100 > 80
                || (double) usedNonHeapMemory / maxNonHeapMemory * 100 > 80) {
            return Health.down()
                    .withDetail("Heap Memory Usage", usedHeapMemory)
                    .withDetail("Non-Heap Memory Usage", usedNonHeapMemory)
                    .build();
        }

        return Health.up()
                .withDetail("Heap Memory Usage", usedHeapMemory)
                .withDetail("Non-Heap Memory Usage", usedNonHeapMemory)
                .build();
    }
}