package org.example.facade;

import lombok.extern.slf4j.Slf4j;
import org.example.auth.TraineeAuth;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.mapper.TraineeMapper;
import org.example.services.TraineeService;
import org.example.validation.TraineeValidation;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TraineeFacade {
    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;
    private final TraineeValidation traineeValidation;
    private final TraineeAuth traineeAuth;

    /**
     * Injecting {@code TraineeFacade} dependencies.
     */
    public TraineeFacade(TraineeService traineeService,
                         TraineeMapper traineeMapper,
                         TraineeValidation traineeValidation,
                         TraineeAuth traineeAuth) {
        this.traineeService = traineeService;
        this.traineeMapper = traineeMapper;
        this.traineeValidation = traineeValidation;
        this.traineeAuth = traineeAuth;
    }


    /**
     * Creates a trainee in the facade layer.
     *
     * @param traineeDto {@code TraineeDto} to create the {@code TraineeEntity}
     */

    public void createTrainee(TraineeDto traineeDto) {
        log.info("Request to create trainee");
        traineeValidation.validateTrainee(traineeDto);
        TraineeEntity traineeEntity = traineeMapper.dtoToEntity(traineeDto);
        traineeService.createTrainee(traineeEntity);
    }

    /**
     * Gets trainee by id in the facade layer.
     *
     * @param id id of the trainee
     * @return the {@code TraineeDto}
     */
    public TraineeDto getTraineeById(Long id) {
        log.info("Request to retrieve trainee by id");
        TraineeEntity trainee = traineeService.getTraineeById(id);
        return traineeMapper.entityToDto(trainee);
    }


    /**
     * Gets trainee by username.
     *
     * @param username username of the trainee
     * @return {@code TraineeDto}
     */
    public TraineeDto getTraineeByUsername(String username) {
        log.info("Request to retrieve trainee by username");
        TraineeEntity trainee = traineeService.getTraineeByUsername(username);
        return traineeMapper.entityToDto(trainee);
    }

    /**
     * Changes trainee password.
     *
     * @param username username of the trainee
     * @param password password of the trainee
     */
    public void changeTraineePassword(String username, String password) {
        log.info("Request to change trainee password.");
        if (traineeAuth.traineeAuth(username, password)) {
            traineeService.changeTraineePassword(username);
        }
    }

    /**
     * Activates trainee.
     *
     * @param id id of the Trainee
     */
    public void activateTrainee(Long id) {
        log.info("Request to activate trainee with id: {}", id);
        traineeService.activateTrainee(id);
    }

    /**
     * Deactivates trainee.
     *
     * @param id id of the Trainee
     */
    public void deactivateTrainee(Long id) {
        log.info("Request to deactivate trainee with id: {}", id);
        traineeService.deactivateTrainee(id);
    }

    /**
     * Updates Trainee by id.
     *
     * @param id         id of the trainee to update
     * @param traineeDto new trainee data to update with
     */
    public void updateTraineeById(Long id, TraineeDto traineeDto) {
        log.info("Request to update trainee by id");
        traineeValidation.validateTrainee(traineeDto);
        traineeService.updateTraineeById(id, traineeMapper.dtoToEntity(traineeDto));
    }


    /**
     * Deletes trainee by username.
     *
     * @param username username of the trainee to delete
     */
    public void deleteTraineeByUsername(String username) {
        log.info("Request to delete trainee by username");
        traineeService.deleteTraineeByUsername(username);
    }

}
