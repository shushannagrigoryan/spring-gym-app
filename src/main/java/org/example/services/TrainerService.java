package org.example.services;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.password.PasswordGeneration;
import org.example.repository.TrainerRepository;
import org.example.username.UsernameGenerator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGeneration passwordGeneration;
    private final TrainingTypeService trainingTypeService;
    private final UserService userService;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainerService(TrainerRepository trainerRepository,
                          UsernameGenerator usernameGenerator,
                          PasswordGeneration passwordGeneration,
                          TrainingTypeService trainingTypeService,
                          UserService userService) {
        this.trainerRepository = trainerRepository;
        this.usernameGenerator = usernameGenerator;
        this.passwordGeneration = passwordGeneration;
        this.trainingTypeService = trainingTypeService;
        this.userService = userService;
    }

    /**
     * Creates Trainer in the Service layer.
     *
     * @param trainerEntity {@code TrainerEntity} to create
     */
    @Transactional
    public TrainerEntity registerTrainer(TrainerEntity trainerEntity) {
        log.debug("Creating trainer: {}", trainerEntity);
        TrainingTypeEntity trainingType =
                trainingTypeService.getTrainingTypeById(trainerEntity.getSpecializationId());
        trainerEntity.setSpecialization(trainingType);
        String username = usernameGenerator.generateUsername(
                trainerEntity.getUser().getFirstName(),
                trainerEntity.getUser().getLastName());
        trainerEntity.getUser().setUsername(username);
        trainerEntity.getUser().setPassword(passwordGeneration.generatePassword());
        userService.registerUser(trainerEntity.getUser());
        TrainerEntity trainer = trainerRepository.save(trainerEntity);
        log.debug("Successfully created a new trainer with username: {}", username);
        return trainer;
    }

    //    /**
    //     * Gets trainer by username.
    //     * If no trainer is found returns null.
    //     *
    //     * @param username username of the trainer
    //     * @return the {@code TrainerEntity}
    //     */
    //    @Transactional
    //    public TrainerEntity getTrainerByUsername(String username) {
    //        log.debug("Retrieving trainer by username: {}", username);
    //        TrainerEntity trainer = trainerRepository.getTrainerByUsername(username);
    //        if (trainer == null) {
    //            log.debug("No trainer with the username: {}", username);
    //            throw new GymEntityNotFoundException(
    //            String.format("Trainer with username %s does not exist.", username));
    //        }
    //        log.debug("Successfully retrieved trainer by username: {}", username);
    //        return trainer;
    //    }
    //
    //    /**
    //     * Gets trainer by id.
    //     * If there is no trainer with the given id throws an {@code GymIllegalIdException}
    //     *
    //     * @param id of the trainer
    //     * @return the TrainerDao
    //     */
    //    @Transactional
    //    public TrainerEntity getTrainerById(Long id) {
    //        log.debug("Retrieving trainer by id: {}", id);
    //        TrainerEntity trainer = trainerRepository.getTrainerById(id);
    //        if (trainer == null) {
    //            throw new GymIllegalIdException(String.format("No trainer with id: %d", id));
    //        }
    //        log.debug("Successfully retrieved trainer with id: {}", id);
    //        return trainer;
    //    }
    //
    //    /**
    //     * Changes the password of the trainer by username. Before changing authentication is performed.
    //     *
    //     * @param username username of the trainer
    //     */
    //    @Transactional
    //    public void changeTrainerPassword(String username) {
    //        log.debug("Changing the password of the trainee: {}", username);
    //        trainerRepository.changeTrainerPassword(username,
    //                passwordGeneration.generatePassword());
    //    }
    //
    //    /**
    //     * Updates trainer by id.
    //     *
    //     * @param id            id of the trainer
    //     * @param trainerEntity {@code TrainerEntity} to update with
    //     */
    //    @Transactional
    //    public void updateTrainerById(Long id, TrainerEntity trainerEntity) {
    //        log.debug("Updating trainer by id: {}", id);
    //        TrainerEntity trainer = trainerRepository.getTrainerById(id);
    //
    //        if (trainer == null) {
    //            throw new GymIllegalIdException(String.format("No trainer with id: %d", id));
    //        }
    //
    //        String updatedFirstName = trainerEntity.getUser().getFirstName();
    //        String updatedLastName = trainerEntity.getUser().getLastName();
    //        String firstName = trainer.getUser().getFirstName();
    //        String lastName = trainer.getUser().getLastName();
    //
    //        if (!((firstName.equals(updatedFirstName)) && (lastName.equals(updatedLastName)))) {
    //            String username = usernameGenerator
    //                    .generateUsername(updatedFirstName, updatedLastName);
    //            trainer.getUser().setUsername(username);
    //            trainer.getUser().setFirstName(updatedFirstName);
    //            trainer.getUser().setLastName(updatedLastName);
    //        }
    //
    //        long trainingId = trainerEntity.getSpecializationId();
    //        TrainingTypeEntity specialization = trainingTypeService.getTrainingTypeById(trainingId);
    //        if (specialization == null) {
    //            throw new GymIllegalIdException(String.format("Illegal id for training: %d", trainingId));
    //        }
    //        trainer.setSpecialization(specialization);
    //        trainerRepository.updateTrainerById(id, trainer);
    //        log.debug("Successfully updated trainer with id: {}", id);
    //    }
    //
    //    /**
    //     * Activates Trainer.
    //     *
    //     * @param id id of the trainer
    //     */
    //    @Transactional
    //    public void activateTrainer(Long id) {
    //        log.info("Request to activate trainer with id: {}", id);
    //        TrainerEntity trainer = trainerRepository.getTrainerById(id);
    //
    //        if (trainer == null) {
    //            log.debug("No trainer with {} exists.", id);
    //            throw new GymIllegalIdException(String.format("No trainer with %d exists.", id));
    //        }
    //
    //        if (trainer.getUser().isActive()) {
    //            log.debug("Trainer with id: {} is already active.", id);
    //            throw new GymIllegalStateException(String.format("Trainer with id: %d is already active", id));
    //        }
    //
    //        trainerRepository.activateTrainer(trainer);
    //
    //    }
    //
    //    /**
    //     * Deactivates Trainer.
    //     *
    //     * @param id id of the trainer
    //     */
    //    @Transactional
    //    public void deactivateTrainer(Long id) {
    //        log.info("Request to deactivate trainer with id: {}", id);
    //        TrainerEntity trainer = trainerRepository.getTrainerById(id);
    //
    //        if (trainer == null) {
    //            log.debug("No entity with {} exists.", id);
    //            throw new GymIllegalIdException(String.format("No entity with %d exists.", id));
    //        }
    //
    //        if (!trainer.getUser().isActive()) {
    //            log.debug("Trainer with id: {} is already inactive.", id);
    //            throw new GymIllegalStateException(String.format("Trainer with id: %d is already inactive", id));
    //        }
    //
    //        trainerRepository.deactivateTrainer(trainer);
    //
    //    }
    //
    //    /**
    //     * Returns trainers not assigned to trainee by trainee username.
    //     * Throws GymIllegalUsernameException if the username is not valid.
    //     *
    //     * @param traineeUsername of the trainee.
    //     * @return {@code List<TrainerEntity>}
    //     */
    //
    //    @Transactional
    //    public List<TrainerEntity> getTrainersNotAssignedToTrainee(String traineeUsername) {
    //        return trainerRepository.getTrainersNotAssignedToTrainee(traineeUsername);
    //    }

}
