package org.example.auth;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.services.TraineeService;
import org.springframework.stereotype.Component;

// traineeAuth and trainerAuth methods are very similar to each other 
// in terms of logic, which I think is problematic, because if 
// authentication logic changes, then both TraineeAuth and 
// TrainerAuth will need to get edited.
// I would move the authentication method to UserService instead, 
// since authentication relies only on
// User Entity data, and then use that method wherever it is needed.

@Component
@Slf4j
public class TraineeAuth {
    private final TraineeService traineeService;

    /**
     * Injecting dependencies using constructor.
     */
    public TraineeAuth(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    /**
     * Authentication for trainee by username and password.
     *
     * @param username username of the trainee
     * @param password password of the trainee
     * @return true if trainee exists, else false.
     */
    public boolean traineeAuth(String username, String password) {
        TraineeEntity trainee = traineeService.getTraineeByUsername(username);
        if (trainee == null) {
            log.debug("No trainee with username: {}", username);
            throw new GymIllegalArgumentException(
                    String.format("No trainee with username: %s", username));
        }

        if (!trainee.getUser().getPassword().equals(password)) {
            log.debug("Incorrect password for trainee with username: {}", username);
            throw new GymIllegalArgumentException(
                    String.format("Incorrect password for trainee with username: %s",
                            username));
        }

        return true;
    }

}
