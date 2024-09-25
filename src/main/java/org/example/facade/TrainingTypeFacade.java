package org.example.facade;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingTypeDto;
import org.example.entity.TrainingTypeEntity;
import org.example.mapper.TrainingTypeMapper;
import org.example.services.TrainingTypeService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainingTypeFacade {
    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper trainingTypeMapper;

    public TrainingTypeFacade(TrainingTypeService trainingTypeService,
                              TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeService = trainingTypeService;
        this.trainingTypeMapper = trainingTypeMapper;
    }

    /**
     * Gets training type by id in the facade layer.
     *
     * @param id id of the training type
     * @return the {@code TrainingDto}
     */
    public TrainingTypeDto getTrainingTypeById(Long id) {
        log.info("Request to retrieve training type by id");
        TrainingTypeEntity trainingType = trainingTypeService.getTrainingTypeById(id);
        return trainingTypeMapper.entityToDto(trainingType);
    }
}
