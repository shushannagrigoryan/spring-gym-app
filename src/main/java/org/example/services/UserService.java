package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
//import org.example.entity.Trainer;
import org.example.entity.TrainerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    @Autowired
    private Map<Long, TraineeEntity> traineeMap;

    @Autowired
    private Map<Long, TrainerEntity> trainerMap;

    private TraineeService traineeService;
    @Autowired
    public void setDependencies(TraineeService traineeService){
        this.traineeService = traineeService;
    }

    String generateUsername(String firstName, String lastName) {
        String username = firstName + lastName;
        TraineeDto trainee = null;
        try{
            trainee = traineeService.getTraineeByUsername(username);
        }catch(Exception e){
            log.debug("no trainee with username: " + username + e.getMessage());
        }
//        if((trainee != null) || (trainerMap.get(username) != null)){
//            logger.debug("Username taken.");
//            return username + getSuffix(username);
//        }
        if((trainee != null) || (trainerMap.get(username) != null)){
            log.debug("Username taken.");
            return username + getSuffix(username);
        }
        return username;
    }

    private Long getSuffix(String username){
        log.debug("Generating suffix for username: " + username);
        long suffix = 0L;
//        Set<Long> traineeSuffixSet = traineeMap.keySet().stream().filter(key -> key.startsWith(username))
//                .map(key -> key.substring(username.length()))
//                .filter(s -> !s.isEmpty()).map(Long::valueOf).collect(Collectors.toSet());

        Set<Long> traineeSuffixSet = traineeMap.values().stream().map(TraineeEntity::getUsername).filter(u -> u.startsWith(username))
                .map(key -> key.substring(username.length()))
                .filter(s -> !s.isEmpty()).map(Long::valueOf).collect(Collectors.toSet());

        Set<Long> trainerSuffixSet = trainerMap.values().stream().map(TrainerEntity::getUsername).filter(u -> u.startsWith(username))
                .map(key -> key.substring(username.length()))
                .filter(s -> !s.isEmpty()).map(Long::valueOf).collect(Collectors.toSet());

//        Set<Long> trainerSuffixSet = trainerMap.keySet().stream().filter(key -> key.startsWith(username))
//                .map(key -> key.substring(username.length()))
//                .filter(s -> !s.isEmpty()).map(Long::valueOf).collect(Collectors.toSet());

        traineeSuffixSet.addAll(trainerSuffixSet);

        if(!traineeSuffixSet.isEmpty()){
            suffix = Collections.max(traineeSuffixSet) + 1;

        }

        return suffix;
    }


}
