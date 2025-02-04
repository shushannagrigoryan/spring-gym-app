package org.example.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.controller.TrainerWorkloadClient;
import org.example.dto.requestdto.ActionType;
import org.example.dto.requestdto.TrainerWorkloadRequestDto;
import org.example.dto.requestdto.UpdateTrainerWorkloadRequestDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.entity.TrainingEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadService {
    private final TrainerWorkloadClient trainerWorkloadClient;

    private static UpdateTrainerWorkloadRequestDto getTrainerWorkloadRequestDto(TrainingEntity trainingEntity,
                                                                                ActionType actionType) {
        UpdateTrainerWorkloadRequestDto workloadDto = new UpdateTrainerWorkloadRequestDto();
        workloadDto.setUsername(trainingEntity.getTrainer().getUser().getUsername());
        workloadDto.setFirstName(trainingEntity.getTrainer().getUser().getFirstName());
        workloadDto.setLastName(trainingEntity.getTrainer().getUser().getLastName());
        workloadDto.setIsActive(trainingEntity.getTrainer().getUser().isActive());
        workloadDto.setTrainingDate(trainingEntity.getTrainingDate());
        workloadDto.setTrainingDuration(trainingEntity.getTrainingDuration());
        workloadDto.setActionType(actionType);
        return workloadDto;
    }

    /**
     * Calling TrainerWorkloadService to update trainer's workload after adding/deleting a training.
     *
     * @param trainingEntity training added/deleted
     * @param actionType     Add/Delete
     */
    @CircuitBreaker(name = "updateTrainerWorkload", fallbackMethod = "fallbackMethodForUpdateWorkload")
    public void updateTrainerWorkload(TrainingEntity trainingEntity, ActionType actionType) {
        UpdateTrainerWorkloadRequestDto workloadDto = getTrainerWorkloadRequestDto(trainingEntity, actionType);

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
        throw new RuntimeException("Update trainer workload service is currently not available.");
    }

    /**
     * Calling TrainerWorkloadService to get trainer's workload after adding/deleting a training.
     */

    @CircuitBreaker(name = "getTrainerWorkload", fallbackMethod = "fallbackMethodForGetWorkload")
    public BigDecimal getTrainerWorkload(TrainerWorkloadRequestDto trainerWorkloadRequestDto) {
        ResponseEntity<ResponseDto<BigDecimal>> response =
            trainerWorkloadClient.getWorkload(trainerWorkloadRequestDto.getUsername(),
                trainerWorkloadRequestDto.getTrainingYear(),
                trainerWorkloadRequestDto.getTrainingMonth());

        if (response.getBody() != null) {
            return response.getBody().getPayload();
        }
        return BigDecimal.ZERO;
    }

    /**
     * Fallback method for circuit breaker for getting trainer workload.
     */
    public BigDecimal fallbackMethodForGetWorkload(TrainerWorkloadRequestDto trainerWorkloadRequestDto,
                                                Throwable throwable) {
        log.debug("Running the fallback method for getTraineeWorkload with trainerWorkload: {}.",
            trainerWorkloadRequestDto);
        log.debug(throwable.getMessage());
        throw new RuntimeException("Update trainer workload service is currently not available.");
    }
}
