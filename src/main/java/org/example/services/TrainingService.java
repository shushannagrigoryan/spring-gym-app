package org.example.services;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.repository.TrainingRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainingService {
    private final TrainingRepository trainingRepository;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    /**
     * Gets training by id.
     *
     * @param id of the training
     * @return the {@code TrainingEntity}
     */
    public TrainingEntity getTrainingById(Long id) {
        log.debug("Retrieving training by id: {}", id);
        Optional<TrainingEntity> training = trainingRepository.getTrainingById(id);
        if (training.isEmpty()) {
            log.debug("Invalid id for training: {}", id);
            throw new GymIllegalIdException(String.format("No training with id: %d", id));
        }
        log.debug("Getting training with id: {}", id);
        return training.get();
    }


}
