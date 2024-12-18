package org.example.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.dto.responsedto.TrainerProfileResponseDto;
import org.example.dto.responsedto.TrainerProfileTraineeResponseDto;
import org.example.dto.responsedto.TrainerUpdateResponseDto;
import org.example.dto.responsedto.TrainingTypeResponseDto;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainerProfileMapper {
    private final TrainingTypeMapper trainingTypeMapper;
    private final TraineeMapper traineeMapper;

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
        Set<TrainerProfileTraineeResponseDto> trainees = trainingEntityList
            .stream()
            .map(x -> traineeMapper.entityToTrainerTraineeResponseDto(x.getTrainee()))
            .collect(Collectors.toSet());

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

        Set<TrainerProfileTraineeResponseDto> trainees = updatedTrainer.getTrainings()
            .stream()
            .map(x -> traineeMapper.entityToTrainerTraineeResponseDto(x.getTrainee()))
            .collect(Collectors.toSet());

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
