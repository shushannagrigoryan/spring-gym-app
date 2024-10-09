package org.example.services;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.UserEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalStateException;
import org.example.password.PasswordGeneration;
import org.example.repository.TraineeRepository;
import org.example.username.UsernameGenerator;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class TraineeService {
    private final TraineeRepository traineeRepository;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGeneration passwordGeneration;
    private final UserService userService;

    /**
     * Injecting dependencies using constructor.
     */
    public TraineeService(TraineeRepository traineeRepository,
                          UsernameGenerator usernameGenerator,
                          PasswordGeneration passwordGeneration,
                          UserService userService) {
        this.traineeRepository = traineeRepository;
        this.usernameGenerator = usernameGenerator;
        this.passwordGeneration = passwordGeneration;
        this.userService = userService;
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
        UserEntity user = userService.registerUser(traineeEntity.getUser());
        log.debug("Successfully registered user: {}", user);
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
                .findByUser_Username(username);
        if (trainee.isEmpty()) {
            log.debug("No trainee with the username: {}", username);
            throw new GymEntityNotFoundException(
                    String.format("Trainee with username %s does not exist.", username));
        }
        log.debug("Successfully retrieved trainee by username: {}", username);
        return trainee.get();
    }
    //
    //    /**
    //     * Gets the trainee by id in the service layer.
    //     * If no trainee was found throws an {@code GymIllegalIdException}
    //     *
    //     * @param id id of the trainee to get
    //     * @return the {@code TraineeEntity}
    //     */
    //    @Transactional
    //    public TraineeEntity getTraineeById(Long id) {
    //        log.debug("Retrieving trainee by id: {}", id);
    //        TraineeEntity trainee = traineeRepository.getTraineeById(id);
    //        if (trainee == null) {
    //            throw new GymIllegalIdException(String.format("No trainee with id: %d", id));
    //        }
    //        log.debug("Successfully retrieved trainee by id: {}", id);
    //        return trainee;
    //    }
    //

    //    /**
    //     * Changes the password of the trainee by username. Before changing authentication is performed.
    //     *
    //     * @param username username of the trainee
    //     * @param password password of the trainee
    //     */
    //    @Transactional
    //    public void changeTraineePassword(String username, String password) {
    //        log.debug("Changing the password of the trainee: {} ", username);
    //        userService.changeUserPassword(username, passwordGeneration.generatePassword());
    //        //traineeRepository.changeTraineePassword(username, passwordGeneration.generatePassword());
    //    }
    //

    /**
     * Activates Trainee.
     *
     * @param id id of the trainee
     */
    @Transactional
    public void activateTrainee(Long id) {
        log.info("Request to activate trainee with id: {}", id);
        Optional<TraineeEntity> trainee = traineeRepository.findById(id);

        if (trainee.isEmpty()) {
            log.debug("No trainee with {} exists.", id);
            throw new GymIllegalIdException(String.format("No trainee with %d exists.", id));
        }

        if (trainee.get().getUser().isActive()) {
            log.debug("Trainee with id: {} is already active.", id);
            throw new GymIllegalStateException(String.format("Trainee with id: %d is already active", id));
        }

        //traineeRepository.updateByUser_Active(true);
        //traineeRepository.activateTrainee(trainee);
    }
    //
    //    /**
    //     * Deactivates Trainee.
    //     *
    //     * @param id id of the trainee
    //     */
    //    @Transactional
    //    public void deactivateTrainee(Long id) {
    //        log.info("Request to deactivate trainee with id: {}", id);
    //        TraineeEntity trainee = traineeRepository.getTraineeById(id);
    //
    //        if (trainee == null) {
    //            log.debug("No entity with {} exists.", id);
    //            throw new GymIllegalIdException(String.format("No entity with %d exists.", id));
    //        }
    //
    //        if (!trainee.getUser().isActive()) {
    //            log.debug("Trainee with id: {} is already inactive.", id);
    //            throw new GymIllegalStateException(String.format("Trainee with id: %d is already inactive", id));
    //        }
    //
    //        traineeRepository.deactivateTrainee(trainee);
    //
    //    }

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

        Optional<TraineeEntity> trainee = traineeRepository.findByUser_Username(username);

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
        log.debug("Deleting trainee by username: {}", username);
        //traineeRepository.deleteByUserUsername(username);
        log.debug("Successfully deleted trainee by username: {}", username);
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
        Optional<TraineeEntity> trainee = traineeRepository.findByUser_Username(username);
        if (trainee.isEmpty()) {
            log.debug("No trainee with the username: {}", username);
            throw new GymEntityNotFoundException(
                    String.format("Trainee with username %s does not exist.", username));
        }
        log.debug("Lazily initializing trainee trainings: {}", trainee.get().getTrainings());

        log.debug("Successfully retrieved trainee profile by username: {}", username);
        return trainee.get();
    }

}
