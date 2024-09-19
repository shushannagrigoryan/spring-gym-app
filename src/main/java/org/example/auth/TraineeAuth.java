package org.example.auth;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.TraineeDao;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymIllegalArgumentException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TraineeAuth {
    private final TraineeDao traineeDao;

    /**
     * Injecting dependencies using constructor.
     */
    public TraineeAuth(TraineeDao traineeDao) {
        this.traineeDao = traineeDao;
    }

    /**
     * Authentication for trainee by username and password.
     *
     * @param username username of the trainee
     * @param password password of the trainee
     * @return true if trainee exists, else false.
     */
    public boolean traineeAuth(String username, String password) {
        Optional<TraineeEntity> trainee = traineeDao.getTraineeByUsername(username);
        if (trainee.isEmpty()) {
            log.debug("No trainee with username: {}", username);
            throw new GymIllegalArgumentException(
                    String.format("No trainee with username: %s", username));
        }

        if (!trainee.get().getUser().getPassword().equals(password)) {
            log.debug("Incorrect password for trainee with username: {}", username);
            throw new GymIllegalArgumentException(
                    String.format("Incorrect password for trainee with username: %s", username));
        }

        return true;
    }

}
