package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingTypeDto;
import org.example.entity.TrainingType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainingTypeMapper {

    /**
     * Maps a {@code TrainingType} to a {@code TrainingTypeDto}.
     */
    public TrainingTypeDto entityToDto(TrainingType trainingType) {
        log.info("Mapping TrainerType {} to TrainingTypeDto", trainingType);
        if (trainingType == null) {
            return null;
        }

        return new TrainingTypeDto(trainingType.getName());
    }

    /**
     * Maps a {@code TrainerDto} to a {@code TrainerEntity}.
     */
    public TrainingType dtoToEntity(TrainingTypeDto trainingTypeDto) {
        log.info("Mapping TrainingTypeDto {} to TrainingType", trainingTypeDto);
        if (trainingTypeDto == null) {
            return null;
        }

        TrainingType trainingType = new TrainingType();
        trainingType.setName(trainingTypeDto.getName());

        return trainingType;
    }
}
