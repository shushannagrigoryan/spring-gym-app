package org.example.services;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.responsedto.TrainerResponseDto;
import org.example.entity.Role;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.password.PasswordGeneration;
import org.example.repositories.TrainerRepository;
import org.example.username.UsernameGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGeneration passwordGeneration;
    private final TrainingTypeService trainingTypeService;
    private final TraineeService traineeService;
    private final UserService userService;


    /**
     * Injecting dependencies using constructor.
     */
    public TrainerService(TrainerRepository trainerRepository,
                          UsernameGenerator usernameGenerator,
                          PasswordGeneration passwordGeneration,
                          TrainingTypeService trainingTypeService,
                          @Lazy TraineeService traineeService,
                          UserService userService) {
        this.trainerRepository = trainerRepository;
        this.usernameGenerator = usernameGenerator;
        this.passwordGeneration = passwordGeneration;
        this.trainingTypeService = trainingTypeService;
        this.traineeService = traineeService;
        this.userService = userService;
    }

    /**
     * Creates Trainer in the Service layer.
     *
     * @param trainerEntity {@code TrainerEntity} to create
     */
    @Transactional
    public TrainerResponseDto registerTrainer(TrainerEntity trainerEntity) {
        log.debug("Creating trainer: {}", trainerEntity);
        TrainingTypeEntity trainingType =
            trainingTypeService.getTrainingTypeById(trainerEntity.getSpecializationId());
        trainerEntity.setSpecialization(trainingType);
        String username = usernameGenerator.generateUsername(trainerEntity.getUser());
        String password = passwordGeneration.generatePassword();
        trainerEntity.getUser().setUsername(username);
        trainerEntity.getUser().setPassword(password);
        trainerEntity.getUser().setRoles(Set.of(Role.TRAINER));
        userService.save(trainerEntity.getUser());
        TrainerEntity trainer = trainerRepository.save(trainerEntity);
        log.debug("Successfully created a new trainer: {}", trainer);
        return new TrainerResponseDto(username, password);
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

    /**
     * Gets trainer by id.
     * If there is no trainer with the given id throws an {@code GymIllegalIdException}
     *
     * @param id of the trainer
     * @return the TrainerDao
     */
    @Transactional
    public TrainerEntity getTrainerById(Long id) {
        log.debug("Retrieving trainer by id: {}", id);
        Optional<TrainerEntity> trainer = trainerRepository.findById(id);
        if (trainer.isEmpty()) {
            throw new GymEntityNotFoundException(String.format("No trainer with id: %d", id));
        }
        log.debug("Successfully retrieved trainer with id: {}", id);
        return trainer.get();
    }


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
        Long specialization = trainerToUpdate.getSpecializationId();

        if (!Objects.equals(trainerEntity.getSpecialization().getId(), specialization)) {
            throw new GymIllegalArgumentException(String.format("No trainer with username: %s and specializationId: %d",
                username, specialization));
        }
        trainerEntity.getUser().setFirstName(trainerToUpdate.getUser().getFirstName());
        trainerEntity.getUser().setLastName(trainerToUpdate.getUser().getLastName());
        trainerEntity.getUser().setActive(trainerToUpdate.getUser().isActive());

        TrainerEntity updatedTrainer = trainerRepository.save(trainerEntity);
        log.debug("Successfully updated trainer with username: {}", username);
        return updatedTrainer;
    }

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
        TrainerEntity trainer = trainerRepository.findByUser_Username(username)
            .orElseThrow(() -> new GymEntityNotFoundException(String.format("Trainer with username %s does not exist.",
                username)));

        if (trainer.getUser().isActive() == isActive) {
            log.debug("Trainer : {} isActive status is already: {}", username, isActive);
            return "Trainer isActive status is already " + isActive;
        }

        log.debug("Setting trainer: {} isActive status to {}", username, isActive);
        trainer.getUser().setActive(isActive);
        trainerRepository.save(trainer);

        return "Successfully set trainer active status to " + isActive;
    }

    /**
     * Returns active trainers which are not assigned to trainee with the given username.
     *
     * @param traineeUsername username of the trainee
     * @return {@code Set<TrainerEntity>}
     */
    @Transactional
    public List<TrainerEntity> notAssignedOnTraineeActiveTrainers(String traineeUsername) {
        log.debug("Getting active trainers which are not assigned on trainee: {}", traineeUsername);
        TraineeEntity trainee = traineeService.getTraineeByUsername(traineeUsername);
        return trainerRepository
            .findByTrainingsTraineeNotInAndUserActive(Set.of(trainee), true);
    }
}
