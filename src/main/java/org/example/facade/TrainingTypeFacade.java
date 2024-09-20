package org.example.facade;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingTypeDto;
import org.example.exceptions.GymIllegalIdException;
import org.example.services.TrainingTypeService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainingTypeFacade {
    private final TrainingTypeService trainingTypeService;

    public TrainingTypeFacade(TrainingTypeService trainingTypeService) {
        this.trainingTypeService = trainingTypeService;
    }

    /**
     * Gets training type by id in the facade layer.
     *
     * @param id id of the training type
     * @return the {@code TrainingDto}
     */
    public TrainingTypeDto getTrainingTypeById(Long id) {
        log.info("Request to retrieve training type by id");
        TrainingTypeDto trainingTypeDto = null;
        try {
            trainingTypeDto = trainingTypeService.getTrainingTypeById(id);
            log.info("Successfully retrieved trainingType by id");
        } catch (GymIllegalIdException exception) {
            log.error("No trainingType with id: {}", id, exception);
        }
        return trainingTypeDto;
    }
}
