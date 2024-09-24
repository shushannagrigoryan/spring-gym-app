package org.example.facade;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingDto;
import org.example.entity.TrainingEntity;
import org.example.mapper.TrainingMapper;
import org.example.services.TrainingCreationService;
import org.example.services.TrainingService;
import org.example.validation.TrainingValidation;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainingFacade {
    private final TrainingCreationService trainingCreationService;
    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;
    private final TrainingValidation trainingValidation;

    /**
     * Injecting {@code TrainingFacade} dependencies.
     */
    public TrainingFacade(TrainingService trainingService,
                          TrainingMapper trainingMapper,
                          TrainingValidation trainingValidation,
                          TrainingCreationService trainingCreationService) {
        this.trainingCreationService = trainingCreationService;
        this.trainingService = trainingService;
        this.trainingMapper = trainingMapper;
        this.trainingValidation = trainingValidation;
    }

    /**
     * Gets the training by id in the facade layer.
     *
     * @param id id of the training
     * @return returns the {@code TrainingDto}
     */
    public TrainingDto getTrainingById(Long id) {
        log.info("Request to retrieve training by id");
        TrainingEntity trainingDto;
        trainingDto = trainingService.getTrainingById(id);
        return trainingMapper.entityToDto(trainingDto);
    }
}
