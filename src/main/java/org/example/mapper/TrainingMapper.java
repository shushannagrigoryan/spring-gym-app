package org.example.mapper;

import org.example.dto.TrainingDto;
import org.example.entity.TrainingEntity;
import org.springframework.stereotype.Component;

@Component
public class TrainingMapper {

    /**
     * Maps a TrainingEntity to a TrainingDto.
     */
    public TrainingDto entityToDto(TrainingEntity trainingEntity) {
        if (trainingEntity == null) {
            return null;
        }

        return new TrainingDto(trainingEntity.getTrainerId(), trainingEntity.getTraineeId(),
                trainingEntity.getTrainingName(), trainingEntity.getTrainingType(),
                trainingEntity.getTrainingDate(), trainingEntity.getTrainingDuration());
    }

    /**
     * Maps a TrainingDto to a TrainingEntity.
     */
    public TrainingEntity dtoToEntity(TrainingDto trainingDto) {
        if (trainingDto == null) {
            return null;
        }

        TrainingEntity trainingEntity = new TrainingEntity();
        trainingEntity.setTraineeId(trainingDto.getTraineeId());
        trainingEntity.setTrainerId(trainingDto.getTrainerId());
        trainingEntity.setTrainingName(trainingDto.getTrainingName());
        trainingEntity.setTrainingType(trainingDto.getTrainingType());
        trainingEntity.setTrainingDate(trainingDto.getTrainingDate());
        trainingEntity.setTrainingDuration(trainingDto.getTrainingDuration());

        return trainingEntity;
    }
}
