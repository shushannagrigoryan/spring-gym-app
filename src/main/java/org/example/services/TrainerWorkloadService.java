package org.example.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.requestdto.ActionType;
import org.example.dto.requestdto.TrainerWorkloadRequestDto;
import org.example.dto.requestdto.UpdateTrainerWorkloadRequestDto;
import org.example.dto.responsedto.GetTrainerWorkloadResponseDto;
import org.example.dto.responsedto.ResponseDto;
import org.example.entity.TrainingEntity;
import org.example.mapper.TrainingMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TrainerWorkloadService {
    private final TrainingMapper trainingMapper;
    private final UpdateTrainerWorkloadSenderService updateTrainerWorkloadSenderService;
    private final TrainerWorkloadSenderService trainerWorkloadSenderService;
    private final GetWorkloadService getWorkloadService;

    /**
     * Calling TrainerWorkloadService to update trainer's workload after adding/deleting a training.
     *
     * @param trainingEntity training added/deleted
     * @param actionType     Add/Delete
     */
    @CircuitBreaker(name = "updateTrainerWorkload", fallbackMethod = "fallbackMethodForUpdateWorkload")
    @Transactional
    public void updateTrainerWorkload(TrainingEntity trainingEntity, ActionType actionType) {
        log.debug("updating trainer workload.");
        UpdateTrainerWorkloadRequestDto workloadDto =
            trainingMapper.getTrainerWorkloadRequestDto(trainingEntity, actionType);
        updateTrainerWorkloadSenderService.send(workloadDto);
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
    public ResponseEntity
        <ResponseDto<GetTrainerWorkloadResponseDto>> getTrainerWorkload(
        TrainerWorkloadRequestDto trainerWorkloadRequestDto) {

        log.debug("Running getTrainerWorkloadAsync.");

        trainerWorkloadSenderService.send(trainerWorkloadRequestDto);

        return getWorkloadService.getWorkload(trainerWorkloadRequestDto);

    }

    /**
     * Fallback method for circuit breaker for getting trainer workload.
     */
    public ResponseEntity
        <ResponseDto<GetTrainerWorkloadResponseDto>> fallbackMethodForGetWorkload(
        TrainerWorkloadRequestDto trainerWorkloadRequestDto,
        Throwable throwable) {
        log.debug("Running the fallback method for getTraineeWorkload with trainerWorkload: {}.",
            trainerWorkloadRequestDto);
        log.debug(throwable.getMessage());
        throw new RuntimeException("Trainer workload service is currently not available.");
    }
}
