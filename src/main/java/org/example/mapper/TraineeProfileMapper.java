package org.example.mapper;

import java.util.List;
import org.example.dto.TraineeProfileResponseDto;
import org.example.dto.TraineeProfileTrainerResponseDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainingEntity;
import org.springframework.stereotype.Component;

@Component
public class TraineeProfileMapper {
    private final TrainerMapper trainerMapper;

    /** Setting dependencies. */
    public TraineeProfileMapper(TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

    /** Maps trainee entity to trainee profile response dto. */
    public TraineeProfileResponseDto entityToProfileDto(TraineeEntity traineeEntity) {
        if (traineeEntity == null) {
            return null;
        }
        TraineeProfileResponseDto traineeProfile = new TraineeProfileResponseDto();
        traineeProfile.setFirstName(traineeEntity.getUser().getFirstName());
        traineeProfile.setLastName(traineeEntity.getUser().getLastName());
        traineeProfile.setDateOfBirth(traineeEntity.getDateOfBirth());
        traineeProfile.setAddress(traineeEntity.getAddress());
        traineeProfile.setActive(traineeProfile.isActive());
        List<TrainingEntity> trainingEntityList = traineeEntity.getTrainings();
        List<TraineeProfileTrainerResponseDto> trainers = trainingEntityList
                .stream()
                .map(x -> trainerMapper.entityToTraineeTrainerResponseDto(x.getTrainer()))
                .toList();

        traineeProfile.setTrainers(trainers);
        return traineeProfile;
    }
}
