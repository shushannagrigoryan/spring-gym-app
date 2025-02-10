package org.example.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.ActionType;
import org.example.dto.requestdto.TrainerWorkloadRequestDto;
import org.example.dto.requestdto.UpdateTrainerWorkloadRequestDto;
import org.example.dto.responsedto.GetTrainerWorkloadResponseDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.entity.TrainingEntity;
import org.example.mapper.TrainingMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadService {
    private final TrainingMapper trainingMapper;
    private final UpdateTrainerWorkloadSenderService updateTrainerWorkloadSenderService;
    private final TrainerWorkloadSenderService trainerWorkloadSenderService;
    private final JmsTemplate jmsTemplate;


    /**
     * Calling TrainerWorkloadService to update trainer's workload after adding/deleting a training.
     *
     * @param trainingEntity training added/deleted
     * @param actionType     Add/Delete
     */
    @CircuitBreaker(name = "updateTrainerWorkload", fallbackMethod = "fallbackMethodForUpdateWorkload")
    public void updateTrainerWorkload(TrainingEntity trainingEntity, ActionType actionType) {
        UpdateTrainerWorkloadRequestDto workloadDto =
            trainingMapper.getTrainerWorkloadRequestDto(trainingEntity, actionType);
        updateTrainerWorkloadSenderService.send(workloadDto);

//        ResponseEntity<ResponseDto<String>> response = trainerWorkloadClient.updateWorkload(workloadDto);
//        if (response.getBody() != null) {
//            log.debug(response.getBody().getPayload());
//        }

        log.debug(String.format("Successfully updated trainer's %s workload", workloadDto.getUsername()));
    }

    /**
     * Fallback method for circuit breaker for updating trainer workload.
     */
    public void fallbackMethodForUpdateWorkload(TrainingEntity trainingEntity, ActionType actionType,
                                                Throwable throwable) {
        log.debug("Running the fallback method for updateTraineeWorkload with training: {} and actionType: {}.",
            trainingEntity, actionType);
        log.debug(throwable.getMessage());
        throw new RuntimeException("Trainer workload service is currently not available.");
    }

    //    /**
    //     * Calling TrainerWorkloadService to get trainer's workload after adding/deleting a training.
    //     */
    //
    //    @CircuitBreaker(name = "getTrainerWorkload", fallbackMethod = "fallbackMethodForGetWorkload")
    //    public GetTrainerWorkloadResponseDto getTrainerWorkload(TrainerWorkloadRequestDto trainerWorkloadRequestDto) {
    //        ResponseEntity<ResponseDto<BigDecimal>> response = trainerWorkloadClient.getWorkload(
    //            trainerWorkloadRequestDto.getUsername(),
    //            trainerWorkloadRequestDto.getTrainingYear(),
    //            trainerWorkloadRequestDto.getTrainingMonth());
    //        GetTrainerWorkloadResponseDto responseDto = new GetTrainerWorkloadResponseDto(
    //            trainerWorkloadRequestDto.getUsername(),
    //            trainerWorkloadRequestDto.getTrainingYear(),
    //            trainerWorkloadRequestDto.getTrainingMonth());
    //
    //        if (response.getBody() != null) {
    //            responseDto.setWorkload(response.getBody().getPayload());
    //            return responseDto;
    //        }
    //
    //        responseDto.setWorkload(BigDecimal.ZERO);
    //        return responseDto;
    //    }


    public CompletableFuture<ResponseEntity<ResponseDto<GetTrainerWorkloadResponseDto>>> getTrainerWorkload(
        TrainerWorkloadRequestDto trainerWorkloadRequestDto) {

        log.debug("Running getTrainerWorkloadAsync.");
        CompletableFuture<ResponseEntity<ResponseDto<GetTrainerWorkloadResponseDto>>> responseFuture =
            new CompletableFuture<>();
        String responseQueue = "response.queue." + UUID.randomUUID();

        trainerWorkloadSenderService.send(trainerWorkloadRequestDto, responseQueue);

        new Thread(() -> {
            try {
                log.debug("Handling response.");
                String trainerWorkload = (String) jmsTemplate.receiveAndConvert(responseQueue);

                if (trainerWorkload != null) {
                    log.debug("data != null");
                    GetTrainerWorkloadResponseDto responseDto = new GetTrainerWorkloadResponseDto(
                        trainerWorkloadRequestDto.getUsername(),
                        trainerWorkloadRequestDto.getTrainingYear(),
                        trainerWorkloadRequestDto.getTrainingMonth());
                    responseDto.setWorkload(new BigDecimal(trainerWorkload));
                    responseFuture.complete(
                        ResponseEntity.ok(new ResponseDto<>(responseDto, "Successfully retrieved trainer's workload"))
                    );
                } else {
                    log.debug("data == null");
                    responseFuture.complete(
                        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            new ResponseDto<>(null, "Error processing the workload request"))
                    );
                }
            } catch (Exception e) {
                log.debug("exception occurred");
                //TODO throw new RuntimeException("Trainer workload service is currently not available.")
                responseFuture.complete(
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new ResponseDto<>(null, "Error processing the workload request"))
                );
            }
        }).start();

        log.debug("Returning response from service.");
        return responseFuture;
    }
}
