package org.example.auth;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.TrainerEntity;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.services.TrainerService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainerAuth {
    private final TrainerService trainerService;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainerAuth(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    /**
     * Authentication for trainer by username and password.
     *
     * @param username username of the trainer
     * @param password password of the trainer
     * @return true if trainer exists, else false.
     */
    public boolean trainerAuth(String username, String password) {
        TrainerEntity trainer = trainerService.getTrainerByUsername(username);
        if (trainer == null) {
            log.debug("No trainer with username: {}", username);
            throw new GymIllegalArgumentException(
                    String.format("No trainer with username: %s", username));
        }

        if (!trainer.getUser().getPassword().equals(password)) {
            log.debug("Incorrect password for trainer with username: {}", username);
            throw new GymIllegalArgumentException(
                    String.format("Incorrect password for trainer with username: %s", username));
        }

        return true;
    }

}
