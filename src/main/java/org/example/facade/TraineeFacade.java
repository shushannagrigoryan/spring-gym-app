package org.example.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.dto.TrainingDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainingEntity;
import org.example.mapper.TraineeMapper;
import org.example.mapper.TrainingMapper;
import org.example.services.TraineeService;
import org.example.validation.TraineeValidation;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TraineeFacade {
    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;
    private final TraineeValidation traineeValidation;
    private final TrainingMapper trainingMapper;

    /**
     * Injecting {@code TraineeFacade} dependencies.
     */
    public TraineeFacade(TraineeService traineeService,
                         TraineeMapper traineeMapper,
                         TraineeValidation traineeValidation,
                         TrainingMapper trainingMapper) {
        this.traineeService = traineeService;
        this.traineeMapper = traineeMapper;
        this.traineeValidation = traineeValidation;
        this.trainingMapper = trainingMapper;
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
        TraineeEntity trainee;
        trainee = traineeService.getTraineeById(id);
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
        TraineeEntity trainee;
        trainee = traineeService.getTraineeByUsername(username);
        log.info("Successfully retrieved trainee by username");
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
        traineeService.changeTraineePassword(username, password);
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

    /**
     * Returns trainees trainings list by trainee username and given criteria.
     *
     * @param traineeUsername username of the trainee
     * @param fromDate        training fromDate
     * @param toDate          training toDate
     * @param trainingTypeId  training type
     * @param trainerUsername trainer username
     * @return {@code List<TrainingDto>}
     */
    public List<TrainingDto> getTraineeTrainingsByFilter(String traineeUsername, LocalDate fromDate,
                                                         LocalDate toDate, Long trainingTypeId,
                                                         String trainerUsername) {
        log.debug("Request to get trainees trainings by trainee username: {} "
                        + "and criteria: fromDate:{} toDate:{} trainingType: {} trainerUsername: {}",
                traineeUsername, fromDate, toDate, trainingTypeId, trainerUsername);

        List<TrainingEntity> trainingList;
        trainingList = traineeService
                .getTraineeTrainingsByFilter(traineeUsername, fromDate, toDate, trainingTypeId, trainerUsername);

        assert trainingList != null;
        return trainingList.stream().map(trainingMapper::entityToDto).collect(Collectors.toList());



    }

    //    /**
    //     * Updates trainee's trainer list.
    //     *
    //     * @param traineeUsername          trainee username
    //     * @param trainingsUpdatedTrainers map with key: training id to update, value: new trainer id
    //     */
    //    public void updateTraineesTrainersList(String traineeUsername, Map<Long, Long> trainingsUpdatedTrainers) {
    //        log.info("Request to update trainers list of trainee: {}", traineeUsername);
    //
    //        try {
    //            traineeService.updateTraineesTrainersList(traineeUsername, trainingsUpdatedTrainers);
    //        } catch (GymIllegalUsernameException | GymDataUpdateException exception) {
    //            log.error(exception.getMessage(), exception);
    //        }
    //    }

}
