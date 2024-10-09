package org.example.services;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalArgumentException;
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

    /**
     * Gets trainer by username.
     * If no trainer is found returns null.
     *
     * @param username username of the trainer
     * @return the {@code TrainerEntity}
     */
    public TrainerEntity getTrainerByUsername(String username) {
        log.debug("Retrieving trainer by username: {}", username);
        Optional<TrainerEntity> trainer = trainerRepository.findByUser_Username(username);
        if (trainer.isEmpty()) {
            log.debug("No trainer with the username: {}", username);
            throw new GymEntityNotFoundException(
            String.format("Trainer with username %s does not exist.", username));
        }
        log.debug("Successfully retrieved trainer by username: {}", username);
        return trainer.get();
    }
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



    /**
     * Updates trainer.
     *
     * @param trainerToUpdate trainer data to update
     * @return {@code TrainerEntity} updated trainer
     */
    @Transactional
    public TrainerEntity updateTrainer(TrainerEntity trainerToUpdate) {
        String username = trainerToUpdate.getUser().getUsername();
        log.debug("Updating trainer with username: {}", trainerToUpdate.getUser().getUsername());

        Optional<TrainerEntity> trainer = trainerRepository.findByUser_Username(username);

        if (trainer.isEmpty()) {
            log.debug("No trainer with username: {}", username);
            throw new GymIllegalArgumentException(String.format("No trainer with username: %s", username));
        }
        TrainerEntity trainerEntity = trainer.get();
        trainerEntity.getUser().setFirstName(trainerToUpdate.getUser().getFirstName());
        trainerEntity.getUser().setLastName(trainerToUpdate.getUser().getLastName());

        TrainerEntity updatedTrainer = trainerRepository.save(trainerEntity);

        List<TrainingEntity> trainings = updatedTrainer.getTrainings();
        log.debug("Lazily initialized trainer trainings {}", trainings);

        TrainingTypeEntity specialization = updatedTrainer.getSpecialization();
        log.debug("Lazily initialized trainer specialization {}", specialization);


        log.debug("Successfully updated trainer with username: {}", username);
        return updatedTrainer;
    }




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

    /**
     * Returns trainer profile.
     * Throws {@code GymEntityNotFoundException if trainer is not found.}
     *
     * @param username username of the trainer
     * @return {@code TrainerEntity}
     */
    @Transactional
    public TrainerEntity getTrainerProfile(String username) {
        log.debug("Getting trainer profile by username: {}", username);
        Optional<TrainerEntity> trainer = trainerRepository.findByUser_Username(username);
        if (trainer.isEmpty()) {
            log.debug("No trainer with the username: {}", username);
            throw new GymEntityNotFoundException(
                    String.format("Trainer with username %s does not exist.", username));
        }
        List<TrainingEntity> trainings = trainer.get().getTrainings();
        log.debug("Lazily initialized trainer trainings: {}", trainings);

        TrainingTypeEntity specialization = trainer.get().getSpecialization();
        log.debug("Lazily initialized trainer specialization: {}", specialization);

        log.debug("Successfully retrieved trainer profile by username: {}", username);
        return trainer.get();
    }

    /**
     * Activates/Deactivates trainer. Not Idempotent action.
     *
     * @param username username of the trainer
     * @param isActive activate if true, deactivate if false.
     */
    @Transactional
    public String changeActiveStatus(String username, boolean isActive) {
        TrainerEntity trainer = getTrainerByUsername(username);

        if (trainer.getUser().isActive() == isActive) {
            log.debug("Trainer : {} isActive status is already: {}", username, isActive);
            return "Trainer isActive status is already " + isActive;
        }

        log.debug("Setting trainer: {} isActive status to {}", username, isActive);
        trainer.getUser().setActive(isActive);
        trainerRepository.save(trainer);

        return "Successfully set trainer active status to " + isActive;
    }
}
