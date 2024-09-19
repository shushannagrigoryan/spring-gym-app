package org.example.auth;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.TrainerDao;
import org.example.entity.TrainerEntity;
import org.example.exceptions.GymIllegalArgumentException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainerAuth {
    private final TrainerDao trainerDao;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainerAuth(TrainerDao trainerDao) {
        this.trainerDao = trainerDao;
    }

    /**
     * Authentication for trainer by username and password.
     *
     * @param username username of the trainer
     * @param password password of the trainer
     * @return true if trainer exists, else false.
     */
    public boolean trainerAuth(String username, String password) {
        Optional<TrainerEntity> trainer = trainerDao.getTrainerByUsername(username);
        if (trainer.isEmpty()) {
            log.debug("No trainer with username: {}", username);
            throw new GymIllegalArgumentException(
                    String.format("No trainer with username: %s", username));
        }

        if (!trainer.get().getUser().getPassword().equals(password)) {
            log.debug("Incorrect password for trainer with username: {}", username);
            throw new GymIllegalArgumentException(
                    String.format("Incorrect password for trainer with username: %s", username));
        }

        return true;
    }

}
