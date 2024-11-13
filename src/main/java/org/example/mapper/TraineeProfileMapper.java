package org.example.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.dto.responsedto.TraineeProfileResponseDto;
import org.example.dto.responsedto.TraineeProfileTrainerResponseDto;
import org.example.dto.responsedto.TraineeUpdateResponseDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainingEntity;
import org.springframework.stereotype.Component;

@Component
public class TraineeProfileMapper {
    private final TrainerMapper trainerMapper;

    /**
     * Setting dependencies.
     */
    public TraineeProfileMapper(TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    /**
     * Maps trainee entity to trainee profile response dto.
     */
    public TraineeProfileResponseDto entityToProfileDto(TraineeEntity traineeEntity) {
        if (traineeEntity == null) {
            return null;
        }
        TraineeProfileResponseDto traineeProfile = new TraineeProfileResponseDto();
        traineeProfile.setFirstName(traineeEntity.getUser().getFirstName());
        traineeProfile.setLastName(traineeEntity.getUser().getLastName());
        traineeProfile.setDateOfBirth(traineeEntity.getDateOfBirth());
        traineeProfile.setAddress(traineeEntity.getAddress());
        traineeProfile.setActive(traineeEntity.getUser().isActive());
        List<TrainingEntity> trainingEntityList = traineeEntity.getTrainings();
        Set<TraineeProfileTrainerResponseDto> trainers = trainingEntityList
            .stream()
            .map(x -> trainerMapper.entityToTraineeTrainerResponseDto(x.getTrainer()))
            .collect(Collectors.toSet());

        traineeProfile.setTrainers(trainers);
        return traineeProfile;
    }

    /**
     * Mapping updated trainee entity to response dto.
     */
    public TraineeUpdateResponseDto entityToUpdatedDto(TraineeEntity updatedTrainee) {
        if (updatedTrainee == null) {
            return null;
        }

        Set<TraineeProfileTrainerResponseDto> trainers = updatedTrainee.getTrainings()
            .stream()
            .map(x -> trainerMapper.entityToTraineeTrainerResponseDto(x.getTrainer()))
            .collect(Collectors.toSet());

        return new TraineeUpdateResponseDto(updatedTrainee.getUser().getUsername(),
            updatedTrainee.getUser().getFirstName(),
            updatedTrainee.getUser().getLastName(),
            updatedTrainee.getDateOfBirth(),
            updatedTrainee.getAddress(),
            updatedTrainee.getUser().isActive(),
            trainers);

    }
}
