package org.example.facade;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.exceptions.IllegalIdException;
import org.example.exceptions.IllegalPasswordException;
import org.example.exceptions.IllegalUsernameException;
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
     */
    public void createTrainee(TraineeDto traineeDto) {
        log.info("Request to create trainee");
        TraineeEntity traineeEntity = traineeMapper.dtoToEntity(traineeDto);
        try {
            traineeService.createTrainee(traineeEntity);
            log.info("Successfully created trainee");
        } catch (IllegalPasswordException exception) {
            log.error(exception.getMessage(), exception);
        }
    }

    /**
     * Gets the trainee by username in the facade layer.
     * If there is no trainee with the given username, throws a IllegalUsernameException.
     */
    public TraineeDto getTraineeByUsername(String username) {
        log.info("Request to retrieve trainee by username");
        TraineeDto traineeDto;
        try {
            traineeDto = traineeService.getTraineeByUsername(username);
            log.info("Successfully retrieved trainee by username");
        } catch (IllegalUsernameException exception) {
            log.error(exception.getMessage());
            throw exception;
        }
        return traineeDto;
    }

    /**
     * Gets Trainee by id in the facade layer.
     * If there is no trainee with the given id throws an IllegalIdException.
     */
    public TraineeDto getTraineeById(Long id) {
        log.info("Request to retrieve trainee by id");
        TraineeDto traineeDto;
        try {
            traineeDto = traineeService.getTraineeById(id);
            log.info("Successfully retrieved trainee by id");
        } catch (IllegalIdException exception) {
            log.error(exception.getMessage());
            throw exception;
        }
        return traineeDto;
    }

    /**
     * Deletes Trainee by id.
     * If there is no trainee with the given id, throws an IllegalIdException
     */
    public void deleteTraineeById(Long id) {
        log.info("Request to delete trainee by id");
        try {
            traineeService.deleteTraineeById(id);
            log.info("Successfully deleted trainee by id");
        } catch (IllegalIdException exception) {
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
        } catch (IllegalPasswordException | IllegalIdException exception) {
            log.error(exception.getMessage(), exception);
        }

    }


}
