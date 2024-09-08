package org.example.facade;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalPasswordException;
import org.example.mapper.TraineeMapper;
import org.example.services.TraineeService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TraineeFacade {
    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;

    public TraineeFacade(TraineeService traineeService, TraineeMapper traineeMapper) {
        this.traineeService = traineeService;
        this.traineeMapper = traineeMapper;
    }

    /**
     * Creates a trainee in the facade layer.
     *
     * @param traineeDto {@code TraineeDto} to create the {@code TraineeEntity}
     */
    public void createTrainee(TraineeDto traineeDto) {
        log.info("Request to create trainee");
        TraineeEntity traineeEntity = traineeMapper.dtoToEntity(traineeDto);
        try {
            traineeService.createTrainee(traineeEntity);
            log.info("Successfully created trainee");
        } catch (GymIllegalPasswordException exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    /**
     * Gets trainee by id in the facade layer.
     *
     * @param id id of the trainee
     * @return the {@code TraineeDto}
     */
    public TraineeDto getTraineeById(Long id) {
        log.info("Request to retrieve trainee by id");
        TraineeDto traineeDto = null;
        try {
            traineeDto = traineeService.getTraineeById(id);
            log.info("Successfully retrieved trainee by id");
        } catch (GymIllegalIdException exception) {
            log.error(exception.getMessage());
        }
        return traineeDto;
    }

    /**
     * Deletes trainee by id.
     *
     * @param id id of the trainee to delete
     */
    public void deleteTraineeById(Long id) {
        log.info("Request to delete trainee by id");
        try {
            traineeService.deleteTraineeById(id);
            log.info("Successfully deleted trainee by id");
        } catch (GymIllegalIdException exception) {
            log.error("No trainee with id: {} for deleting", id, exception);
        }

    }

    /**
     * Updates Trainee by id.
     *
     * @param id         id of the trainee to update
     * @param traineeDto new trainee data to update with
     */
    public void updateTraineeById(Long id, TraineeDto traineeDto) {
        log.info("Request to update trainee by id");
        try {
            traineeService.updateTraineeById(id, traineeMapper.dtoToEntity(traineeDto));
            log.info("Successfully updated trainee by id");
        } catch (GymIllegalPasswordException | GymIllegalIdException exception) {
            log.error(exception.getMessage(), exception);
        }

    }


}
