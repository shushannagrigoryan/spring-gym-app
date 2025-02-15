package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalStateException;
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

    /**
     * Injecting dependencies using constructor.
     */
    public TraineeService(TraineeRepository traineeRepository,
                          UsernameGenerator usernameGenerator,
                          PasswordGeneration passwordGeneration) {
        this.traineeRepository = traineeRepository;
        this.usernameGenerator = usernameGenerator;
        this.passwordGeneration = passwordGeneration;
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
    @Transactional
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
    @Transactional
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
    @Transactional
    public void changeTraineePassword(String username) {
        log.debug("Changing the password of the trainee: {} ", username);
        traineeRepository.changeTraineePassword(username, passwordGeneration.generatePassword());
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
            log.debug("No trainee with {} exists.", id);
            throw new GymIllegalIdException(String.format("No trainee with %d exists.", id));
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
    @Transactional
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

        traineeRepository.deactivateTrainee(trainee);

    }

    /**
     * Updates trainee by id.
     *
     * @param id            id of the trainee
     * @param traineeEntity {@code TraineeEntity} to update with
     */
    @Transactional
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
    @Transactional
    public void deleteTraineeByUsername(String username) {
        log.debug("Deleting trainee by username: {}", username);
        traineeRepository.deleteTraineeByUsername(username);
        log.debug("Successfully deleted trainee by username: {}", username);
    }

}
