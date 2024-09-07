package org.example.dao;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.exceptions.GymIllegalUsernameException;
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
     */
    public String generateUsername(String firstName, String lastName) {
        log.debug("Generating username for firstName: {}, lastName: {}", firstName, lastName);
        System.out.println("STORAGE");
        System.out.println("Trainee Map: = " + dataStorage.getTraineeStorage());
        System.out.println("Trainer Map: = " + dataStorage.getTrainerStorage());
        String username = firstName + lastName;
        TraineeDto trainee = null;
        TrainerDto trainer = null;
        //try {
        trainee = traineeService.getTraineeByUsername(username);
        trainer = trainerService.getTrainerByUsername(username);

        //        } catch (GymIllegalUsernameException e) {
        //            log.debug("no trainee with username: " + username + e.getMessage());
        //        }

        if ((trainee != null) || (trainer != null)) {
            log.debug("Username taken.");
            return username + getSuffix(username);
        }
        return username;
    }

    /**
     * Getting suffix for the given username if the username is already taken.
     */
    private Long getSuffix(String username) {
        log.debug("Generating suffix for username: " + username);

        Long maxTraineeSuffix = dataStorage.getTraineeStorage().values()
                .stream()
                .map(TraineeEntity::getUsername)
                .filter(u -> u.startsWith(username))
                .map(key -> key.substring(username.length()))
                .filter(s -> !s.isEmpty()).map(Long::valueOf)
                .max(Long::compareTo).orElse(0L);

        Long maxTrainerSuffix = dataStorage.getTrainerStorage().values()
                .stream()
                .map(TrainerEntity::getUsername)
                .filter(u -> u.startsWith(username))
                .map(key -> key.substring(username.length()))
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .max(Long::compareTo).orElse(0L);

        return maxTraineeSuffix > maxTrainerSuffix ? maxTraineeSuffix : maxTrainerSuffix;
    }
}
