package org.example.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.TrainerAuth;
import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.dao.TrainingTypeDao;
import org.example.dto.TrainerDto;
import org.example.dto.TrainingDto;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalStateException;
import org.example.exceptions.GymIllegalUsernameException;
import org.example.mapper.TrainerMapper;
import org.example.mapper.TrainingMapper;
import org.example.mapper.TrainingTypeMapper;
import org.example.password.PasswordGeneration;
import org.example.username.UsernameGenerator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainerService {
    private final TrainerDao trainerDao;
    private final TrainerMapper trainerMapper;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGeneration passwordGeneration;
    private final TrainerAuth trainerAuth;
    private final TrainingTypeService trainingTypeService;
    private final TrainingTypeMapper trainingTypeMapper;
    private final TrainingTypeDao trainingTypeDao;
    private final TrainingMapper trainingMapper;
    private final TraineeDao traineeDao;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainerService(TrainerMapper trainerMapper,
                          TrainerDao trainerDao,
                          UsernameGenerator usernameGenerator,
                          PasswordGeneration passwordGeneration,
                          TrainerAuth trainerAuth,
                          TrainingTypeService trainingTypeService,
                          TrainingTypeMapper trainingTypeMapper,
                          TrainingTypeDao trainingTypeDao,
                          TrainingMapper trainingMapper,
                          TraineeDao traineeDao) {
        this.trainerMapper = trainerMapper;
        this.trainerDao = trainerDao;
        this.usernameGenerator = usernameGenerator;
        this.passwordGeneration = passwordGeneration;
        this.trainerAuth = trainerAuth;
        this.trainingTypeService = trainingTypeService;
        this.trainingTypeMapper = trainingTypeMapper;
        this.trainingTypeDao = trainingTypeDao;
        this.trainingMapper = trainingMapper;
        this.traineeDao = traineeDao;
    }

    /**
     * Creates Trainer in the Service layer.
     *
     * @param trainerEntity {@code TrainerEntity} to create
     */
    public void createTrainer(TrainerEntity trainerEntity) {
        log.debug("Creating trainer: {}", trainerEntity);

        String username = usernameGenerator.generateUsername(
                trainerEntity.getUser().getFirstName(),
                trainerEntity.getUser().getLastName());
        trainerEntity.getUser().setUsername(username);
        trainerEntity.getUser().setPassword(passwordGeneration.generatePassword());

        Optional<TrainingTypeEntity> trainingType =
                trainingTypeDao.getTrainingTypeById(trainerEntity.getSpecializationId());

        trainingType.ifPresentOrElse(trainerEntity::setSpecialization,
                () -> {
                    throw new GymIllegalIdException(
                            String.format("TrainingType with id %d does not exist",
                                    trainerEntity.getSpecializationId()));
                });

        trainerDao.createTrainer(trainerEntity);
        log.debug("Successfully created a new trainer with username: {}", username);
    }

    /**
     * Gets trainer by username.
     * If no trainer is found returns null.
     *
     * @param username username of the trainer
     * @return the {@code TrainerDto}
     */
    public TrainerDto getTrainerByUsername(String username) {
        log.debug("Retrieving trainer by username: {}", username);
        Optional<TrainerEntity> trainer = trainerDao.getTrainerByUsername(username);
        if (trainer.isEmpty()) {
            log.debug("No trainer with the username: {}", username);
            throw new GymEntityNotFoundException(String.format("Trainer with username %s does not exist.", username));
        }
        log.debug("Successfully retrieved trainer by username: {}", username);
        return trainerMapper.entityToDto(trainer.get());
    }

    /**
     * Gets trainer by id.
     * If there is no trainer with the given id throws an {@code GymIllegalIdException}
     *
     * @param id of the trainer
     * @return the TrainerDao
     */
    public TrainerDto getTrainerById(Long id) {
        log.debug("Retrieving trainer by id: {}", id);
        Optional<TrainerEntity> trainer = trainerDao.getTrainerById(id);
        if (trainer.isEmpty()) {
            throw new GymIllegalIdException(String.format("No trainer with id: %d", id));
        }
        log.debug("Successfully retrieved trainer with id: {}", id);
        return trainerMapper.entityToDto(trainer.get());
    }

    /**
     * Changes the password of the trainer by username. Before changing authentication is performed.
     *
     * @param username username of the trainer
     */
    public void changeTrainerPassword(String username, String password) {
        if (trainerAuth.trainerAuth(username, password)) {
            trainerDao.changeTrainerPassword(username, passwordGeneration.generatePassword());
        }
    }

    /**
     * Updates trainer by id.
     *
     * @param id            id of the trainer
     * @param trainerEntity {@code TrainerEntity} to update with
     */
    public void updateTrainerById(Long id, TrainerEntity trainerEntity) {
        log.debug("Updating trainer by id: {}", id);
        Optional<TrainerEntity> trainer = trainerDao.getTrainerById(id);

        if (trainer.isEmpty()) {
            log.debug("No trainer with id: {}", id);
            throw new GymIllegalIdException(String.format("No trainer with id: %d", id));
        }

        TrainerEntity trainerToUpdate = trainer.get();

        String updatedFirstName = trainerEntity.getUser().getFirstName();
        String updatedLastName = trainerEntity.getUser().getLastName();
        String firstName = trainerToUpdate.getUser().getFirstName();
        String lastName = trainerToUpdate.getUser().getLastName();

        if (!((firstName.equals(updatedFirstName)) && (lastName.equals(updatedLastName)))) {
            String username = usernameGenerator
                    .generateUsername(updatedFirstName, updatedLastName);
            trainerToUpdate.getUser().setUsername(username);
        }

        trainerToUpdate.getUser().setFirstName(updatedFirstName);
        trainerToUpdate.getUser().setLastName(updatedLastName);

        Optional<TrainingTypeEntity> trainingType =
                trainingTypeDao.getTrainingTypeById(trainerEntity.getSpecializationId());
        trainingType.ifPresentOrElse(trainerEntity::setSpecialization, () -> {
            throw new GymIllegalIdException(
                    String.format("Training Type with id %d does not exist.",
                            trainerEntity.getSpecializationId()));
        });
        trainerToUpdate.setSpecialization(trainerEntity.getSpecialization());

        trainerDao.updateTrainerById(id, trainerToUpdate);
        log.debug("Successfully updated trainer with id: {}", id);
    }

    /**
     * Activates Trainer.
     *
     * @param id id of the trainer
     */
    public void activateTrainer(Long id) {
        log.info("Request to activate trainer with id: {}", id);
        Optional<TrainerEntity> trainer = trainerDao.getTrainerById(id);

        if (trainer.isEmpty()) {
            log.debug("No entity with {} exists.", id);
            throw new GymIllegalIdException(String.format("No entity with %d exists.", id));
        }

        if (trainer.get().getUser().isActive()) {
            log.debug("Trainer with id: {} is already active.", id);
            throw new GymIllegalStateException(String.format("Trainer with id: %d is already active", id));
        }

        trainerDao.activateTrainer(trainer.get().getUser().getId());

    }

    /**
     * Deactivates Trainer.
     *
     * @param id id of the trainer
     */
    public void deactivateTrainer(Long id) {
        log.info("Request to deactivate trainer with id: {}", id);
        Optional<TrainerEntity> trainer = trainerDao.getTrainerById(id);

        if (trainer.isEmpty()) {
            log.debug("No entity with {} exists.", id);
            throw new GymIllegalIdException(String.format("No entity with %d exists.", id));
        }

        if (!trainer.get().getUser().isActive()) {
            log.debug("Trainer with id: {} is already inactive.", id);
            throw new GymIllegalStateException(String.format("Trainer with id: %d is already inactive", id));
        }

        trainerDao.deactivateTrainer(trainer.get().getUser().getId());

    }

    /**
     * Returns trainers trainings list by trainer username and given criteria.
     *
     * @param trainerUsername username of the trainer
     * @param fromDate training fromDate
     * @param toDate training toDate
     * @param traineeUsername trainee username
     * @return {@code List<TrainingDto>}
     */

    public List<TrainingDto> getTrainerTrainingsByFilter(String trainerUsername, LocalDate fromDate,
                                                         LocalDate toDate, String traineeUsername) {

        Optional<TrainerEntity> trainer = trainerDao.getTrainerByUsername(trainerUsername);
        if (trainer.isEmpty()) {
            throw new GymIllegalUsernameException(
                    String.format("No trainer with username: %s", trainerUsername)
            );
        }

        List<TrainingEntity> trainingEntities =
                trainerDao.getTrainerTrainingsByFilter(trainerUsername, fromDate,
                        toDate, traineeUsername);

        return trainingEntities.stream().map(trainingMapper::entityToDto).collect(Collectors.toList());
    }

    /**
     * Returns trainers not assigned to trainee by trainee username.
     * Throws GymIllegalUsernameException if the username is not valid.
     *
     * @param traineeUsername of the trainee.
     * @return {@code List<TrainerDto>}
     */
    public List<TrainerDto> getTrainersNotAssignedToTrainee(String traineeUsername) {
        List<TrainerEntity> trainers;

        if (traineeDao.getTraineeByUsername(traineeUsername).isEmpty()) {
            throw new GymIllegalUsernameException(String.format(
                    "No trainee with username: %s", traineeUsername));
        }
        trainers = trainerDao.getTrainersNotAssignedToTrainee(traineeUsername);

        return trainers.stream().map(trainerMapper::entityToDto).collect(Collectors.toList());
    }

}
