package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.services.TraineeService;
import org.example.services.TrainerService;
import org.example.storage.DataStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserDao {
    @Autowired
    private DataStorage dataStorage;

    private TraineeService traineeService;
    private TrainerService trainerService;

    @Autowired
    public void setDependencies(TraineeService traineeService, TrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }


    /**
     * Generates username for user.
     * Add suffix if the username is taken either by a trainee or a trainer.
     */
    public String generateUsername(String firstName, String lastName) {
        log.debug("Generating username for firstName: {}, lastName: {}", firstName, lastName);

        String username = firstName.concat(".").concat(lastName);

        TraineeDto trainee = traineeService.getTraineeByUsername(username);
        TrainerDto trainer = trainerService.getTrainerByUsername(username);

        if ((trainee != null) || (trainer != null)) {
            log.debug("Username taken.");
            return username + getSuffix(username);
        }
        return username;
    }

    /**
     * Getting suffix for the given username which is not present both in trainee and trainer maps.
     */
    private Long getSuffix(String username) {
        log.debug("Generating suffix for username: " + username);

        Long maxTraineeSuffix = dataStorage.getTraineeStorage().values()
                .stream()
                .map(TraineeEntity::getUsername)
                .filter(u -> u.startsWith(username))
                .map(key -> key.substring(username.length()))
                .filter(s -> !s.isEmpty()).map(Long::valueOf)
                .max(Long::compareTo).orElse(-1L);

        Long maxTrainerSuffix = dataStorage.getTrainerStorage().values()
                .stream()
                .map(TrainerEntity::getUsername)
                .filter(u -> u.startsWith(username))
                .map(key -> key.substring(username.length()))
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .max(Long::compareTo).orElse(-1L);

        long suffix = maxTraineeSuffix > maxTrainerSuffix ? maxTraineeSuffix : maxTrainerSuffix;

        return suffix + 1;
    }
}
