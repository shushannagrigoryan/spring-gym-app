package org.example.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.TrainingTypeEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.password.PasswordGeneration;
import org.example.repository.TraineeRepository;
import org.example.username.UsernameGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class TraineeService {
    private final TraineeRepository traineeRepository;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGeneration passwordGeneration;
    private final TrainerService trainerService;

    /**
     * Injecting dependencies using constructor.
     */
    public TraineeService(TraineeRepository traineeRepository,
                          UsernameGenerator usernameGenerator,
                          PasswordGeneration passwordGeneration,
                          TrainerService trainerService) {
        this.traineeRepository = traineeRepository;
        this.usernameGenerator = usernameGenerator;
        this.passwordGeneration = passwordGeneration;
        this.trainerService = trainerService;
    }

    /**
     * Creates a new trainee in the service layer.
     *
     * @param traineeEntity the new {@code TraineeEntity}
     */
    @Transactional
    public TraineeEntity registerTrainee(TraineeEntity traineeEntity) {
        log.debug("Creating trainee: {}", traineeEntity);

        String username = usernameGenerator.generateUsername(
                traineeEntity.getUser().getFirstName(),
                traineeEntity.getUser().getLastName());
        traineeEntity.getUser().setUsername(username);
        traineeEntity.getUser().setPassword(passwordGeneration.generatePassword());
        TraineeEntity trainee = traineeRepository.save(traineeEntity);
        log.debug("Successfully registered trainee: {}", traineeEntity);
        return trainee;
    }


    /**
     * Gets trainee by username.
     * If no trainee is found returns null.
     *
     * @param username username of the trainee
     * @return the {@code TraineeEntity}
     */
    public TraineeEntity getTraineeByUsername(String username) {
        log.debug("Retrieving trainee by username: {}", username);
        Optional<TraineeEntity> trainee = traineeRepository
                .findByUsername(username);
        if (trainee.isEmpty()) {
            log.debug("No trainee with the username: {}", username);
            throw new GymEntityNotFoundException(
                    String.format("Trainee with username %s does not exist.", username));
        }
        log.debug("Successfully retrieved trainee by username: {}", username);
        return trainee.get();
    }

    /**
     * Gets the trainee by id in the service layer.
     * If no trainee was found throws an {@code GymIllegalIdException}
     *
     * @param id id of the trainee to get
     * @return the {@code TraineeEntity}
     */
    @Transactional
    public TraineeEntity getTraineeById(Long id) {
        log.debug("Retrieving trainee by id: {}", id);
        Optional<TraineeEntity> trainee = traineeRepository.findById(id);
        if (trainee.isEmpty()) {
            throw new GymEntityNotFoundException(String.format("No trainee with id: %d", id));
        }
        log.debug("Successfully retrieved trainee by id: {}", id);
        return trainee.get();
    }


    /**
     * Updates trainee.
     *
     * @param traineeToUpdate trainee data to update
     * @return {@code TraineeEntity} updated trainee
     */
    @Transactional
    public TraineeEntity updateTrainee(TraineeEntity traineeToUpdate) {
        String username = traineeToUpdate.getUser().getUsername();
        log.debug("Updating trainee with username: {}", traineeToUpdate.getUser().getUsername());

        Optional<TraineeEntity> trainee = traineeRepository.findByUsername(username);

        if (trainee.isEmpty()) {
            log.debug("No trainee with username: {}", username);
            throw new GymIllegalArgumentException(String.format("No trainee with username: %s", username));
        }
        TraineeEntity traineeEntity = trainee.get();

        traineeEntity.getUser().setFirstName(traineeToUpdate.getUser().getFirstName());
        traineeEntity.getUser().setLastName(traineeToUpdate.getUser().getLastName());

        if (traineeToUpdate.getAddress() != null) {
            trainee.get().setAddress(traineeToUpdate.getAddress());
        }

        if (traineeToUpdate.getDateOfBirth() != null) {
            traineeEntity.setDateOfBirth(traineeToUpdate.getDateOfBirth());
        }

        TraineeEntity updatedTrainee = traineeRepository.save(traineeEntity);

        List<TrainingEntity> trainings = updatedTrainee.getTrainings();
        log.debug("Lazily initialized trainee trainings {}", trainings);
        for (TrainingEntity training : trainings) {
            TrainingTypeEntity trainingType = training.getTrainer().getSpecialization();
            log.debug("Lazily initialized trainer specialization {}", trainingType);
        }

        log.debug("Successfully updated trainee with username: {}", username);
        return updatedTrainee;
    }


    /**
     * Deletes a trainee by username in the service layer.
     * If there is no trainee with the given username throws an {@code GymIllegalUsernameException}.
     *
     * @param username the username of the trainee
     */
    @Transactional
    public void deleteTraineeByUsername(String username) {
        log.debug("Deleting trainee with username: {}", username);
        Optional<TraineeEntity> trainee = traineeRepository.findByUsername(username);
        if (trainee.isEmpty()) {
            throw new GymEntityNotFoundException(String.format("Trainee with username: %s does not exist.", username));
        }

        log.debug("Deleting trainee with username: {}", username);
        traineeRepository.delete(trainee.get());
        log.debug("Successfully deleted trainee with username: {}", username);
    }

    /**
     * Returns trainee profile.
     * Throws {@code GymEntityNotFoundException if trainee not found.}
     *
     * @param username username of the trainee
     * @return {@code TraineeEntity}
     */
    @Transactional
    public TraineeEntity getTraineeProfile(String username) {
        log.debug("Getting trainee profile by username: {}", username);
        Optional<TraineeEntity> trainee = traineeRepository.findByUsername(username);
        if (trainee.isEmpty()) {
            log.debug("No trainee with the username: {}", username);
            throw new GymEntityNotFoundException(
                    String.format("Trainee with username %s does not exist.", username));
        }
        log.debug("Lazily initializing trainee trainings: {}", trainee.get().getTrainings());

        log.debug("Successfully retrieved trainee profile by username: {}", username);
        trainee.get().getTrainings().forEach(t -> {
            TrainingTypeEntity specialization = t.getTrainer().getSpecialization();
            log.debug("Lazily initialized trainer specialization: {}", specialization);
        });
        return trainee.get();
    }

    /**
     * Activates/Deactivates trainee. Not Idempotent action.
     *
     * @param username username of the trainee
     * @param isActive activate if true, deactivate if false.
     */
    @Transactional
    public String changeActiveStatus(String username, boolean isActive) {
        TraineeEntity trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new GymEntityNotFoundException(
                        String.format("Trainee with username %s does not exist.", username)));

        if (trainee.getUser().isActive() == isActive) {
            log.debug("Trainee : {} isActive status is already: {}", username, isActive);
            return "Trainee isActive status is already " + isActive;
        }

        log.debug("Setting trainee: {} isActive status to {}", username, isActive);
        trainee.getUser().setActive(isActive);
        traineeRepository.save(trainee);

        return "Successfully set trainee active status to " + isActive;
    }


    /**
     * Updates trainee trainer list.
     *
     * @param username of the trainee.
     * @param trainers List of trainers usernames
     */
    @Transactional
    public Set<TrainerEntity> updateTraineesTrainerList(String username, List<String> trainers) {
        TraineeEntity trainee = traineeRepository.findByUsername(username)
                .orElseThrow(() -> new GymEntityNotFoundException(
                        String.format("Trainee with username %s does not exist.", username)));
        Set<TrainerEntity> trainerEntities = new HashSet<>();
        trainers.forEach(t -> {
            TrainerEntity trainerEntity = trainerService.getTrainerByUsername(t);
            TrainingTypeEntity trainingType = trainerEntity.getSpecialization();
            log.debug("Lazily initialized trainer's specialization {}.", trainingType);
            trainerEntities.add(trainerEntity);
        });
        trainee.setTrainers(trainerEntities);
        traineeRepository.save(trainee);
        return trainerEntities;
    }
}
