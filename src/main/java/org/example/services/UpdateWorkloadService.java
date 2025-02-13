package org.example.services;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.slf4j.MDC;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateWorkloadService {
    private static final String UPDATE_TRAINER_WORKLOAD_RESPONSE_QUEUE = "update-trainer-workload-response-queue";
    private final TrainingService trainingService;
    private CompletableFuture<String> responseFuture;


    /**
     * Confirms trainer workload update succeeded.
     */
    public void confirmWorkloadUpdate(TrainingEntity createdTraining) {
        log.debug("Waiting for workload update confirmation...");

        responseFuture = new CompletableFuture<>();

        String response = null;
        try {
            response = responseFuture.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("Timeout waiting for response from second microservice.");
        } catch (Exception e) {
            System.out.println("Error processing response: " + e.getMessage());
        }

        log.debug("Response from update-trainer-workload-response-queue: {}", response);
        if (response == null) {
            trainingService.deleteById(createdTraining.getId());
            throw new RuntimeException("Trainer workload service is currently not available.");
        }
        log.debug("Successfully updated trainer's workload");
    }


    /** JmsListener for UPDATE_TRAINER_WORKLOAD_RESPONSE_QUEUE.*/
    @JmsListener(destination = UPDATE_TRAINER_WORKLOAD_RESPONSE_QUEUE)
    public void onSuccessMessage(String successMessage, @Headers Map<String, Object> headers) {
        String trace = (String) headers.get("traceId");
        MDC.put("traceId", trace);
        log.debug("update-trainer-workload-response-queue message: {}", successMessage);
        MDC.clear();
        responseFuture.complete(successMessage);
    }

}
