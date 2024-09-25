package org.example.facade;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingDto;
import org.example.entity.TrainingEntity;
import org.example.mapper.TrainingMapper;
import org.example.services.TrainingService;
import org.example.validation.TrainingValidation;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainingFacade {
    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;
    private final TrainingValidation trainingValidation;

    /**
     * Injecting {@code TrainingFacade} dependencies.
     */
    public TrainingFacade(TrainingService trainingService,
                          TrainingMapper trainingMapper,
                          TrainingValidation trainingValidation) {
        this.trainingService = trainingService;
        this.trainingMapper = trainingMapper;
        this.trainingValidation = trainingValidation;
    }


    /**
     * Creates a new training in the facade layer.
     *
     * @param trainingDto {@code TrainingDto} to create the {@code TrainingEntity}
     */
    public void createTraining(TrainingDto trainingDto) {
        log.info("Request to create training");
        trainingValidation.validateTraining(trainingDto);
        trainingService.createTraining(trainingMapper.dtoToEntity(trainingDto));
        log.info("Successfully created training");
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

    /**
     * Updates training with the given id.
     *
     * @param trainingId  id of the training
     * @param trainingDto new training data
     */
    public void updateTraining(Long trainingId, TrainingDto trainingDto) {
        log.info("Request to update training with id: {}", trainingId);
        trainingService.updateTraining(trainingId, trainingMapper.dtoToEntity(trainingDto));
    }
}
