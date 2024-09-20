package org.example.services;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.TrainingTypeDao;
import org.example.dto.TrainingTypeDto;
import org.example.entity.TrainingTypeEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.mapper.TrainingTypeMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainingTypeService {
    private final TrainingTypeDao trainingTypeDao;
    private final TrainingTypeMapper trainingTypeMapper;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainingTypeService(TrainingTypeDao trainingTypeDao,
                          TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeDao = trainingTypeDao;
        this.trainingTypeMapper = trainingTypeMapper;
    }


    /**
     * Gets the training type by id in the service layer.
     * If no training type is found throws an {@code GymIllegalIdException}
     *
     * @param id id of the training type to get
     * @return the {@code Optional<TrainingTypeDto>}
     */
    public TrainingTypeDto getTrainingTypeById(Long id) {
        log.debug("Retrieving trainingType by id: {}", id);
        Optional<TrainingTypeEntity> trainingType = trainingTypeDao.getTrainingTypeById(id);
        if (trainingType.isEmpty()) {
            throw new GymIllegalIdException(String.format("No training type with id: %d", id));
        }
        log.debug("Successfully retrieved training type by id: {}", id);
        return trainingTypeMapper.entityToDto(trainingType.get());

    }



}
