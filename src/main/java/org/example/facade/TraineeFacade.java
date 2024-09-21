package org.example.facade;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymDataUpdateException;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalStateException;
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
        traineeService.createTrainee(traineeEntity);
        log.info("Successfully created trainee");
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
            log.error("No trainee with id: {}", id, exception);
        }
        return traineeDto;
    }


    /**
     * Gets trainee by username.
     *
     * @param username username of the trainee
     * @return {@code TraineeDto}
     */
    public TraineeDto getTraineeByUsername(String username) {
        log.info("Request to retrieve trainee by username");
        TraineeDto traineeDto = null;
        try {
            traineeDto = traineeService.getTraineeByUsername(username);
            log.info("Successfully retrieved trainee by username");
        } catch (GymEntityNotFoundException exception) {
            log.error("No trainee with username: {}", username, exception);
        }
        return traineeDto;
    }

    /**
     * Changes trainee password.
     *
     * @param username username of the trainee
     * @param password password of the trainee
     */
    public void changeTraineePassword(String username, String password) {
        log.info("Request to change trainee password.");
        try {
            traineeService.changeTraineePassword(username, password);
        } catch (GymIllegalArgumentException | GymDataUpdateException e) {
            log.error("Exception while changing password", e);
        }
    }

    /**
     * Activates trainee.
     *
     * @param id id of the Trainee
     */
    public void activateTrainee(Long id) {
        try {
            traineeService.activateTrainee(id);
        } catch (GymIllegalIdException | GymDataUpdateException | GymIllegalStateException exception) {
            log.error("Exception while activating trainee with id: {}", id, exception);
        }
    }

    /**
     * Deactivates trainee.
     *
     * @param id id of the Trainee
     */
    public void deactivateTrainee(Long id) {
        try {
            traineeService.deactivateTrainee(id);
        } catch (GymIllegalIdException | GymDataUpdateException | GymIllegalStateException exception) {
            log.error("Exception while deactivating trainee with id: {}", id, exception);
        }
    }

    //
    //    /**
    //     * Deletes trainee by id.
    //     *
    //     * @param id id of the trainee to delete
    //     */
    //    public void deleteTraineeById(Long id) {
    //        log.info("Request to delete trainee by id");
    //        try {
    //            traineeService.deleteTraineeById(id);
    //            log.info("Successfully deleted trainee by id");
    //        } catch (GymIllegalIdException exception) {
    //            log.error("No trainee with id: {} for deleting", id, exception);
    //        }
    //
    //    }
    //
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
        } catch (GymIllegalIdException | GymDataUpdateException exception) {
            log.error(exception.getMessage(), exception);
        }

    }

    /**
     * Returns the list of trainers which are not assigned to trainee, by trainee username.
     *
     * @param username username of the trainee
     * @return {@code List<TrainerList>}
     */
    public Set<TrainerDto> trainersNotAssignedToTrainee(String username) {
        log.debug("Request to get trainers which are not assigned to the trainee with username: {}", username);

        Set<TrainerDto> trainers = null;
        try {
            trainers = traineeService.trainersNotAssignedToTrainee(username);
        } catch (GymIllegalIdException exception) {
            log.error(exception.getMessage(), exception);
        }

        log.debug("Successfully retrieved trainers which are not assigned to the trainee with username: {}", username);
        return trainers;

    }

}
