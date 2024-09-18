package org.example.username;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.TraineeDao;
import org.example.dao.TrainerDao;
import org.example.dao.UserDao;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.services.TraineeService;
import org.example.services.TrainerService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UsernameGenerator {
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;
    private final UserDao userDao;

    public UsernameGenerator(TraineeDao traineeDao,
                             TrainerDao trainerDao,
                             UserDao userDao) {
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
        this.userDao = userDao;
    }
    /**
     * Generates username for the user (firstName.lastName)
     * Adds suffix if the username is taken either by a trainee or a trainer.
     *
     * @param firstName firstName of the user
     * @param lastName lastName of the user
     * @return the username
     */
    public String generateUsername(String firstName, String lastName) {
        log.debug("Generating username for firstName: {}, lastName: {}", firstName, lastName);

        String username = firstName.concat(".").concat(lastName);

        TraineeEntity trainee = traineeDao.getTraineeByUsername(username);
        TrainerEntity trainer = trainerDao.getTrainerByUsername(username);

        if ((trainee != null) || (trainer != null)) {
            log.debug("Username taken.");
            return username + getSuffix(username);
        }
        return username;
    }


    /**
     * Generates suffix for the given username which is not present both in trainee and trainer maps.
     *
     * @param username username for which suffix is generated
     * @return the suffix
     */
    private Long getSuffix(String username) {
        log.debug("Generating suffix for username: {}", username);
        List<String> allUsernames = userDao.getAllUsernames();

        Long suffix = allUsernames.stream()
                .filter(u -> u.startsWith(username))
                .map(key -> key.substring(username.length()))
                .filter(s -> !s.isEmpty()).map(Long::valueOf)
                .max(Long::compareTo).orElse(0L);

        return suffix + 1;
    }

}
