package org.example.services;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainerEntity;
import org.example.exceptions.IllegalUsernameException;
import org.example.storage.DataStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
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
        String username = firstName + lastName;
        TraineeDto trainee = null;
        TrainerDto trainer = null;
        try {
            trainee = traineeService.getTraineeByUsername(username);
            trainer = trainerService.getTrainerByUsername(username);

        } catch (IllegalUsernameException e) {
            log.debug("no trainee with username: " + username + e.getMessage());
        }

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
        long suffix = 0L;

        Set<Long> traineeSuffixSet = dataStorage.getTraineeStorage().values()
                .stream()
                .map(TraineeEntity::getUsername)
                .filter(u -> u.startsWith(username))
                .map(key -> key.substring(username.length()))
                .filter(s -> !s.isEmpty()).map(Long::valueOf)
                .collect(Collectors.toSet());

        Set<Long> trainerSuffixSet = dataStorage.getTrainerStorage().values()
                .stream()
                .map(TrainerEntity::getUsername)
                .filter(u -> u.startsWith(username))
                .map(key -> key.substring(username.length()))
                .filter(s -> !s.isEmpty())
                .map(Long::valueOf)
                .collect(Collectors.toSet());

        traineeSuffixSet.addAll(trainerSuffixSet);

        if (!traineeSuffixSet.isEmpty()) {
            suffix = Collections.max(traineeSuffixSet) + 1;

        }

        return suffix;
    }


}
