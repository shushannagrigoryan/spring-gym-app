package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingDto;
import org.example.dto.responsedto.TraineeCriteriaTrainingsResponseDto;
import org.example.dto.responsedto.TrainerCriteriaTrainingsResponseDto;
import org.example.entity.TrainingEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainingMapper {
    private final TrainingTypeMapper trainingTypeMapper;

    /**
     * Setting dependencies.
     */
    public TrainingMapper(TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeMapper = trainingTypeMapper;
    }

    /**
     * Maps a {@code TrainingEntity} to a {@code TrainingDto}.
     */
    public TrainingDto entityToDto(TrainingEntity trainingEntity) {
        log.info("Mapping TrainingEntity {} to TrainingDto", trainingEntity);
        if (trainingEntity == null) {
            return null;
        }

        return new TrainingDto(
            trainingEntity.getTrainee().getId(), trainingEntity.getTrainer().getId(),
            trainingEntity.getTrainingName(),
            trainingEntity.getTrainingType().getId(),
            trainingEntity.getTrainingDate(), trainingEntity.getTrainingDuration());
    }

    /**
     * Maps a {@code TrainingDto} to a {@code TrainingEntity}.
     */
    public TrainingEntity dtoToEntity(TrainingDto trainingDto) {
        log.info("Mapping TrainingDto {} to TrainingEntity", trainingDto);
        if (trainingDto == null) {
            return null;
        }

        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTraineeId(trainingDto.getTraineeId());
        trainingEntity.setTrainerId(trainingDto.getTrainerId());
        trainingEntity.setTrainingName(trainingDto.getTrainingName());
        trainingEntity.setTrainingTypeId(trainingDto.getTrainingType());
        trainingEntity.setTrainingDate(trainingDto.getTrainingDate());
        trainingEntity.setTrainingDuration(trainingDto.getTrainingDuration());

        return trainingEntity;
    }

    /**
     * Mapping trainee training entity to training criteria dto.
     */
    public TraineeCriteriaTrainingsResponseDto traineeTrainingsEntityToCriteriaDto(TrainingEntity training) {
        if (training == null) {
            return null;
        }

        return new TraineeCriteriaTrainingsResponseDto(training.getTrainingName(), training.getTrainingDate(),
            trainingTypeMapper.entityToResponseDto(training.getTrainingType()),
            training.getTrainingDuration(), training.getTrainer().getUser().getUsername());
    }

    /**
     * Mapping trainer training entity to training criteria dto.
     */
    public TrainerCriteriaTrainingsResponseDto trainerTrainingsEntityToCriteriaDto(TrainingEntity training) {
        if (training == null) {
            return null;
        }

        return new TrainerCriteriaTrainingsResponseDto(training.getTrainingName(), training.getTrainingDate(),
            trainingTypeMapper.entityToResponseDto(training.getTrainingType()),
            training.getTrainingDuration(), training.getTrainee().getUser().getUsername());
    }

}
