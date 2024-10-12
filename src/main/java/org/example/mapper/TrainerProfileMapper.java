package org.example.mapper;

import java.util.List;
import org.example.dto.responsedto.TrainerProfileResponseDto;
import org.example.dto.responsedto.TrainerProfileTraineeResponseDto;
import org.example.dto.responsedto.TrainerUpdateResponseDto;
import org.example.dto.responsedto.TrainingTypeResponseDto;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.springframework.stereotype.Component;

@Component
public class TrainerProfileMapper {
    private final TrainingTypeMapper trainingTypeMapper;
    private final TraineeMapper traineeMapper;

    /**
     * Setting dependencies.
     */
    public TrainerProfileMapper(TrainingTypeMapper trainingTypeMapper,
                                TraineeMapper traineeMapper) {
        this.trainingTypeMapper = trainingTypeMapper;
        this.traineeMapper = traineeMapper;
    }

    /**
     * maps trainer entity to trainer profile.
     */
    public TrainerProfileResponseDto entityToProfileDto(TrainerEntity trainerEntity) {
        if (trainerEntity == null) {
            return null;
        }
        TrainerProfileResponseDto trainerProfile = new TrainerProfileResponseDto();
        trainerProfile.setFirstName(trainerEntity.getUser().getFirstName());
        trainerProfile.setLastName(trainerEntity.getUser().getLastName());
        TrainingTypeResponseDto trainingType = trainingTypeMapper
                .entityToResponseDto(trainerEntity.getSpecialization());
        trainerProfile.setSpecialization(trainingType);
        trainerProfile.setActive(trainerEntity.getUser().isActive());


        List<TrainingEntity> trainingEntityList = trainerEntity.getTrainings();
        List<TrainerProfileTraineeResponseDto> trainees = trainingEntityList
                .stream()
                .map(x -> traineeMapper.entityToTrainerTraineeResponseDto(x.getTrainee()))
                .toList();

        trainerProfile.setTrainees(trainees);
        return trainerProfile;
    }

    /**
     * Mapping trainer entity to trainer update response dto.
     */
    public TrainerUpdateResponseDto entityToUpdatedDto(TrainerEntity updatedTrainer) {
        if (updatedTrainer == null) {
            return null;
        }

        List<TrainerProfileTraineeResponseDto> trainees = updatedTrainer.getTrainings()
                .stream()
                .map(x -> traineeMapper.entityToTrainerTraineeResponseDto(x.getTrainee()))
                .toList();

        return new TrainerUpdateResponseDto(
                updatedTrainer.getUser().getUsername(),
                updatedTrainer.getUser().getFirstName(),
                updatedTrainer.getUser().getLastName(),
                trainingTypeMapper.entityToResponseDto(updatedTrainer.getSpecialization()),
                updatedTrainer.getUser().isActive(),
                trainees
        );
    }
}
