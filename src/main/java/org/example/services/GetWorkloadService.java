package org.example.services;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.TrainerWorkloadRequestDto;
import org.example.dto.responsedto.GetTrainerWorkloadResponseDto;
import org.example.dto.responsedto.ResponseDto;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetWorkloadService {
    private static final String TRAINER_WORKLOAD_RESPONSE_QUEUE = "trainer-workload-response-queue";
    private CompletableFuture<String> responseFuture;

    /**
     * Gets trainer workload by date.
     */
    public ResponseEntity<ResponseDto<GetTrainerWorkloadResponseDto>> getWorkload(
        TrainerWorkloadRequestDto trainerWorkloadRequestDto) {
        log.debug("Waiting for workload response...");

        responseFuture = new CompletableFuture<>();
        String trainerWorkload = null;
        try {
            trainerWorkload = responseFuture.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("Timeout waiting for response from workload service.");
        } catch (Exception e) {
            log.error("Error processing response: {}", e.getMessage());
        }

        if (trainerWorkload == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseDto<>(null, "INTERNAL_SERVER_ERROR."));
        }

        log.debug("Successfully retrieved trainer workload: {}", trainerWorkload);
        GetTrainerWorkloadResponseDto responseDto = new GetTrainerWorkloadResponseDto(
            trainerWorkloadRequestDto.getUsername(),
            trainerWorkloadRequestDto.getTrainingYear(),
            trainerWorkloadRequestDto.getTrainingMonth()
        );
        responseDto.setWorkload(new BigDecimal(trainerWorkload));

        return ResponseEntity.ok(new ResponseDto<>(responseDto, "Successfully retrieved trainer's workload"));
    }

    @JmsListener(destination = TRAINER_WORKLOAD_RESPONSE_QUEUE)
    public void onSuccessMessage(String successMessage, @Headers Map<String, Object> headers) {
        String trace = (String) headers.get("traceId");
        MDC.put("traceId", trace);
        log.debug("Received message from trainer-workload-response-queue: {}", successMessage);
        MDC.clear();
        responseFuture.complete(successMessage);
    }
}
