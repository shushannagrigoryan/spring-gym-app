package org.example.facade;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainingMapper;
import org.example.services.TrainerService;
import org.example.validation.TrainerValidation;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainerFacade {
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;
    private final TrainerValidation trainerValidation;
    private final TrainingMapper trainingMapper;

    /**
     * Injecting {@code TrainerFacade} dependencies.
     */
    public TrainerFacade(TrainerService trainerService,
                         TrainerMapper trainerMapper,
                         TrainerValidation trainerValidation,
                         TrainingMapper trainingMapper) {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
        this.trainerValidation = trainerValidation;
        this.trainingMapper = trainingMapper;
    }

    /**
     * Creates a new Trainer in the facade layer.
     *
     * @param trainerDto {@code TrainerDto} to create the {@code TrainerEntity}
     */
    public void createTrainer(TrainerDto trainerDto) {
        log.info("Request to create trainer");
        TrainerEntity trainerEntity = trainerMapper.dtoToEntity(trainerDto);
        trainerValidation.validateTrainer(trainerDto);
        trainerService.createTrainer(trainerEntity);
    }

    /**
     * Gets the trainer by id.
     *
     * @param id the id of the trainer
     * @return the TrainerDto
     */
    public TrainerDto getTrainerById(Long id) {
        log.info("Request to retrieve trainer by id");
        TrainerEntity trainer;
        trainer = trainerService.getTrainerById(id);
        return trainerMapper.entityToDto(trainer);
    }


    /**
     * Gets trainer by username.
     *
     * @param username username of the trainer
     * @return {@code TrainerDto}
     */
    public TrainerDto getTrainerByUsername(String username) {
        log.info("Request to retrieve trainer by username");
        TrainerEntity trainer;
        trainer = trainerService.getTrainerByUsername(username);
        return trainerMapper.entityToDto(trainer);
    }

    /**
     * Changes trainer password.
     *
     * @param username username of the trainer
     * @param password password of the trainer
     */

    public void changeTrainerPassword(String username, String password) {
        log.info("Request to change trainer password.");

        if (password == null || password.isEmpty()) {
            log.error("Password is required.");
            return;
        }
        trainerService.changeTrainerPassword(username, password);

    }

    /**
     * Updates Trainer by id.
     *
     * @param id         id of the trainer to update
     * @param trainerDto new trainer data to update with
     */
    public void updateTrainerById(Long id, TrainerDto trainerDto) {
        log.info("Request to update trainer by id");
        trainerValidation.validateTrainer(trainerDto);
        trainerService.updateTrainerById(id, trainerMapper.dtoToEntity(trainerDto));
    }

    /**
     * Activates trainer.
     *
     * @param id id of the Trainer
     */
    public void activateTrainer(Long id) {
        log.info("Request to activate trainer with id: {}", id);
        trainerService.activateTrainer(id);
    }

    /**
     * Deactivates trainer.
     *
     * @param id id of the Trainer
     */
    public void deactivateTrainer(Long id) {
        log.info("Request to deactivate trainer with id: {}", id);
        trainerService.deactivateTrainer(id);
    }


    /**
     * Returns trainers trainings list by trainer username and given criteria.
     *
     * @param trainerUsername username of the trainer
     * @param fromDate        training fromDate
     * @param toDate          training toDate
     * @param traineeUsername trainee username
     * @return {@code List<TrainingDto>}
     */
    public List<TrainingDto> getTrainerTrainingsByFilter(String trainerUsername, LocalDate fromDate,
                                                         LocalDate toDate, String traineeUsername) {
        log.debug("Request to get trainers trainings by trainer username: {} "
                        + "and criteria: fromDate:{} toDate:{} traineeUsername: {}",
                trainerUsername, fromDate, toDate, traineeUsername);

        List<TrainingEntity> trainingList;
        trainingList = trainerService
                .getTrainerTrainingsByFilter(trainerUsername, fromDate, toDate, traineeUsername);

        assert trainingList != null;
        return trainingList.stream().map(trainingMapper::entityToDto).collect(Collectors.toList());

    }

    /**
     * Returns trainers not assigned to trainee by trainee username.
     *
     * @param traineeUsername of the trainee.
     * @return {@code List<TrainerDto>}
     */
    public List<TrainerDto> getTrainersNotAssignedToTrainee(String traineeUsername) {
        log.info("Request to get trainers not assigned to trainee with username: {}", traineeUsername);
        List<TrainerEntity> trainers;
        trainers = trainerService.getTrainersNotAssignedToTrainee(traineeUsername);
        assert trainers != null;
        return trainers.stream().map(trainerMapper::entityToDto).collect(Collectors.toList());
    }
}
