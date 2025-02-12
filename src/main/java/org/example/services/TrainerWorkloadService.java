package org.example.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.TrainerWorkloadClient;
import org.example.dto.requestdto.ActionType;
import org.example.dto.requestdto.TrainerWorkloadRequestDto;
import org.example.dto.requestdto.UpdateTrainerWorkloadRequestDto;
import org.example.dto.responsedto.GetTrainerWorkloadResponseDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.entity.TrainingEntity;
import org.example.mapper.TrainingMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadService {
    private final TrainerWorkloadClient trainerWorkloadClient;
    private final TrainingMapper trainingMapper;


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

        ResponseEntity<ResponseDto<String>> response = trainerWorkloadClient.updateWorkload(workloadDto);
        if (response.getBody() != null) {
            log.debug(response.getBody().getPayload());
        }

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

    /**
     * Calling TrainerWorkloadService to get trainer's workload after adding/deleting a training.
     */

    @CircuitBreaker(name = "getTrainerWorkload", fallbackMethod = "fallbackMethodForGetWorkload")
    public GetTrainerWorkloadResponseDto getTrainerWorkload(TrainerWorkloadRequestDto trainerWorkloadRequestDto) {
        ResponseEntity<ResponseDto<BigDecimal>> response = trainerWorkloadClient.getWorkload(
            trainerWorkloadRequestDto.getUsername(),
            trainerWorkloadRequestDto.getTrainingYear(),
            trainerWorkloadRequestDto.getTrainingMonth());
        GetTrainerWorkloadResponseDto responseDto = new GetTrainerWorkloadResponseDto(
            trainerWorkloadRequestDto.getUsername(),
            trainerWorkloadRequestDto.getTrainingYear(),
            trainerWorkloadRequestDto.getTrainingMonth());
        if (response.getBody() != null) {
            responseDto.setWorkload(response.getBody().getPayload());
            return responseDto;
        }

        responseDto.setWorkload(BigDecimal.ZERO);
        return responseDto;
    }

    /**
     * Fallback method for circuit breaker for getting trainer workload.
     */
    public GetTrainerWorkloadResponseDto fallbackMethodForGetWorkload(
        TrainerWorkloadRequestDto trainerWorkloadRequestDto,
        Throwable throwable) {
        log.debug("Running the fallback method for getTraineeWorkload with trainerWorkload: {}.",
            trainerWorkloadRequestDto);
        log.debug(throwable.getMessage());
        throw new RuntimeException("Trainer workload service is currently not available.");
    }
}
