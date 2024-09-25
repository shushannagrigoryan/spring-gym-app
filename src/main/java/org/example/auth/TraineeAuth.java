package org.example.auth;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.repository.TraineeRepository;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TraineeAuth {
    private final TraineeRepository traineeRepository;

    /**
     * Injecting dependencies using constructor.
     */
    public TraineeAuth(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    /**
     * Authentication for trainee by username and password.
     *
     * @param username username of the trainee
     * @param password password of the trainee
     * @return true if trainee exists, else false.
     */
    public boolean traineeAuth(String username, String password) {
        TraineeEntity trainee = traineeRepository.getTraineeByUsername(username);
        if (trainee == null) {
            log.debug("No trainee with username: {}", username);
            throw new GymIllegalArgumentException(
                    String.format("No trainee with username: %s", username));
        }

        if (!trainee.getUser().getPassword().equals(password)) {
            log.debug("Incorrect password for trainee with username: {}", username);
            throw new GymIllegalArgumentException(
                    String.format("Incorrect password for trainee with username: %s", username));
        }

        return true;
    }

}
