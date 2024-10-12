package org.example.services;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainingTypeEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.repository.TrainingTypeRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class TrainingTypeService {
    private final TrainingTypeRepo trainingTypeRepository;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainingTypeService(TrainingTypeRepo trainingTypeRepository) {
        this.trainingTypeRepository = trainingTypeRepository;
    }


    /**
     * Gets the training type by id in the service layer.
     * If no training type is found throws an {@code GymIllegalIdException}
     *
     * @param id id of the training type to get
     * @return the {@code Optional<TrainingTypeEntity>}
     */
    @Transactional
    public TrainingTypeEntity getTrainingTypeById(Long id) {
        log.debug("Retrieving trainingType by id: {}", id);
        Optional<TrainingTypeEntity> trainingType = trainingTypeRepository.findById(id);

        if (trainingType.isEmpty()) {
            throw new GymIllegalIdException(String.format("No training type with id: %d", id));
        }
        log.debug("Successfully retrieved training type by id: {}", id);
        return trainingType.get();

    }

    /**
     * Returns all training types.
     *
     * @return {@code List<TrainingTypeEntity>}
     */
    @Transactional
    public List<TrainingTypeEntity> getAllTrainingTypes() {
        log.debug("Getting all training types.");
        return trainingTypeRepository.findAll();
    }


}
