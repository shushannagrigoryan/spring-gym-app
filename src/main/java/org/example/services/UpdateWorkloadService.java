package org.example.services;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.springframework.jms.annotation.JmsListener;
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


    @JmsListener(destination = UPDATE_TRAINER_WORKLOAD_RESPONSE_QUEUE)
    public void onSuccessMessage(String successMessage) {
        log.debug("update-trainer-workload-response-queue message: {}", successMessage);
        responseFuture.complete(successMessage);
    }

}
