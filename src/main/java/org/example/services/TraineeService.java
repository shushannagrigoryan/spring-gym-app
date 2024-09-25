package org.example.services;

import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.TraineeAuth;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainingEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalStateException;
import org.example.exceptions.GymIllegalUsernameException;
import org.example.password.PasswordGeneration;
import org.example.repository.TraineeRepository;
import org.example.username.UsernameGenerator;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Slf4j
public class TraineeService {
    private final TraineeRepository traineeRepository;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGeneration passwordGeneration;
    private final TraineeAuth traineeAuth;
    private final SessionFactory sessionFactory;


    /**
     * Injecting dependencies using constructor.
     */
    public TraineeService(TraineeRepository traineeRepository,
                          UsernameGenerator usernameGenerator,
                          PasswordGeneration passwordGeneration,
                          TraineeAuth traineeAuth,
                          SessionFactory sessionFactory) {
        this.traineeRepository = traineeRepository;
        this.usernameGenerator = usernameGenerator;
        this.passwordGeneration = passwordGeneration;
        this.traineeAuth = traineeAuth;
        this.sessionFactory = sessionFactory;
    }

    /**
     * Creates a new trainee in the service layer.
     *
     * @param traineeEntity the new {@code TraineeEntity}
     */
    @Transactional
    public void createTrainee(TraineeEntity traineeEntity) {
        log.debug("Creating trainee: {}", traineeEntity);

        String username = usernameGenerator.generateUsername(
                traineeEntity.getUser().getFirstName(),
                traineeEntity.getUser().getLastName());
        traineeEntity.getUser().setUsername(username);
        traineeEntity.getUser().setPassword(passwordGeneration.generatePassword());

        traineeRepository.createTrainee(traineeEntity);
        log.debug("Successfully created trainee: {}", traineeEntity);
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
        TraineeEntity trainee = traineeRepository.getTraineeByUsername(username);
        if (trainee == null) {
            log.debug("No trainee with the username: {}", username);
            throw new GymEntityNotFoundException(String.format("Trainee with username %s does not exist.", username));
        }
        log.debug("Successfully retrieved trainee by username: {}", username);
        return trainee;
    }

    /**
     * Gets the trainee by id in the service layer.
     * If no trainee was found throws an {@code GymIllegalIdException}
     *
     * @param id id of the trainee to get
     * @return the {@code TraineeEntity}
     */
    public TraineeEntity getTraineeById(Long id) {
        log.debug("Retrieving trainee by id: {}", id);
        TraineeEntity trainee = traineeRepository.getTraineeById(id);
        if (trainee == null) {
            throw new GymIllegalIdException(String.format("No trainee with id: %d", id));
        }
        log.debug("Successfully retrieved trainee by id: {}", id);
        return trainee;

    }

    /**
     * Changes the password of the trainee by username. Before changing authentication is performed.
     *
     * @param username username of the trainee
     */
    public void changeTraineePassword(String username) {
        log.debug("Changing the password of the trainee: {} ", username);
        traineeRepository.changeTraineePassword(username,
                passwordGeneration.generatePassword());

    }

    /**
     * Activates Trainee.
     *
     * @param id id of the trainee
     */
    @Transactional
    public void activateTrainee(Long id) {
        log.info("Request to activate trainee with id: {}", id);
        TraineeEntity trainee = traineeRepository.getTraineeById(id);

        if (trainee == null) {
            log.debug("No entity with {} exists.", id);
            throw new GymIllegalIdException(String.format("No entity with %d exists.", id));
        }

        if (trainee.getUser().isActive()) {
            log.debug("Trainee with id: {} is already active.", id);
            throw new GymIllegalStateException(String.format("Trainee with id: %d is already active", id));
        }

        traineeRepository.activateTrainee(trainee);
    }

    /**
     * Deactivates Trainee.
     *
     * @param id id of the trainee
     */
    public void deactivateTrainee(Long id) {
        log.info("Request to deactivate trainee with id: {}", id);
        TraineeEntity trainee = traineeRepository.getTraineeById(id);

        if (trainee == null) {
            log.debug("No entity with {} exists.", id);
            throw new GymIllegalIdException(String.format("No entity with %d exists.", id));
        }

        if (!trainee.getUser().isActive()) {
            log.debug("Trainee with id: {} is already inactive.", id);
            throw new GymIllegalStateException(String.format("Trainee with id: %d is already inactive", id));
        }

        traineeRepository.deactivateTrainee(trainee.getUser().getId());

    }

    /**
     * Updates trainee by id.
     *
     * @param id            id of the trainee
     * @param traineeEntity {@code TraineeEntity} to update with
     */
    public void updateTraineeById(Long id, TraineeEntity traineeEntity) {
        log.debug("Updating trainee by id: {}", id);
        TraineeEntity trainee = traineeRepository.getTraineeById(id);

        if (trainee == null) {
            log.debug("No trainee with id: {}", id);
            throw new GymIllegalIdException(String.format("No trainee with id: %d", id));
        }

        String updatedFirstName = traineeEntity.getUser().getFirstName();
        String updatedLastName = traineeEntity.getUser().getLastName();
        String firstName = trainee.getUser().getFirstName();
        String lastName = trainee.getUser().getLastName();

        if (!((firstName.equals(updatedFirstName)) && (lastName.equals(updatedLastName)))) {
            String username = usernameGenerator
                    .generateUsername(updatedFirstName, updatedLastName);
            trainee.getUser().setUsername(username);
        }

        trainee.getUser().setFirstName(updatedFirstName);
        trainee.getUser().setLastName(updatedLastName);
        trainee.setDateOfBirth(traineeEntity.getDateOfBirth());
        trainee.setAddress(traineeEntity.getAddress());

        traineeRepository.updateTraineeById(id, trainee);
        log.debug("Successfully updated trainee with id: {}", id);
    }

    /**
     * Deletes a trainee by username in the service layer.
     * If there is no trainee with the given username throws an {@code GymIllegalUsernameException}.
     *
     * @param username the username of the trainee
     */
    public void deleteTraineeByUsername(String username) {
        log.debug("Deleting trainee by username: {}", username);
        TraineeEntity trainee = traineeRepository.getTraineeByUsername(username);
        if (trainee == null) {
            throw new GymIllegalUsernameException(
                    String.format("Illegal username for trainee: %s.", username));
        }
        traineeRepository.deleteTraineeByUsername(username);
        log.debug("Successfully deleted trainee by username: {}", username);
    }

    /**
     * Returns trainees trainings list by trainee username and given criteria.
     *
     * @param traineeUsername username of the trainee
     * @param fromDate        training fromDate
     * @param toDate          training toDate
     * @param trainingTypeId  training type
     * @param trainerUsername trainer username
     * @return {@code List<TrainingEntity>}
     */

    public List<TrainingEntity> getTraineeTrainingsByFilter(String traineeUsername, LocalDate fromDate,
                                                            LocalDate toDate, Long trainingTypeId,
                                                            String trainerUsername) {
        log.debug("Getting trainee trainings by filter");

        TraineeEntity trainee = traineeRepository.getTraineeByUsername(traineeUsername);
        if (trainee == null) {
            throw new GymIllegalUsernameException(
                    String.format("No trainee with username: %s", traineeUsername)
            );
        }

        return traineeRepository.getTraineeTrainingsByFilter(traineeUsername, fromDate,
                toDate, trainingTypeId, trainerUsername);
    }


    //    /**
    //     * Updates trainee's trainer list.
    //     *
    //     * @param traineeUsername          trainee username
    //     * @param trainingsUpdatedTrainers map with key: trainingId to update, value: new trainer id
    //     */
    //    public void updateTraineesTrainersList(String traineeUsername, Map<Long, Long> trainingsUpdatedTrainers) {
    //        log.debug("Updating trainee's trainers list.");
    //
    //        Map<Long, TrainerEntity> updatedTrainers = new HashMap<>();
    //        if (traineeRepository.getTraineeByUsername(traineeUsername).isEmpty()) {
    //            log.debug("No trainee with username");
    //            throw new GymIllegalUsernameException(String.format(
    //                    "No trainee with username: %s", traineeUsername));
    //        }
    //
    //        for (Map.Entry<Long, Long> entry : trainingsUpdatedTrainers.entrySet()) {
    //            Long trainingId = entry.getKey();
    //            Long trainerId = entry.getValue();
    //
    //            TrainerEntity trainer = trainerService.getTrainerById(trainerId);
    //            if (trainer == null) {
    //                log.error("No trainer with id: {}, skipping update for training: {}", trainerId, trainingId);
    //                continue;
    //            }
    //            updatedTrainers.put(trainingId, trainer);
    //        }
    //
    //        traineeRepository.updateTraineesTrainersList(traineeUsername, updatedTrainers);
    //        log.debug("Successfully updated trainee: {} trainers list", traineeUsername);
    //
    //    }
}
