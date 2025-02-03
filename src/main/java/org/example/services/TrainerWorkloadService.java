package org.example.services;

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
    public void updateTrainerWorkload(TrainingEntity trainingEntity, ActionType actionType) {
        UpdateTrainerWorkloadRequestDto workloadDto = getTrainerWorkloadRequestDto(trainingEntity, actionType);

        ResponseEntity<ResponseDto<String>> response  = trainerWorkloadClient.updateWorkload(workloadDto);
        if (response.getBody() != null) {
            log.debug(response.getBody().getPayload());
        }

        log.debug(String.format("Successfully updated trainer's %s workload", workloadDto.getUsername()));
    }

    /**
     * Calling TrainerWorkloadService to get trainer's workload after adding/deleting a training.
     */

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
}
