package org.example.services;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private Map<String, Trainee> traineeMap;
    @Autowired
    private Map<String, Trainer> trainerMap;

    String generateUsername(String firstName, String lastName){
        String username = firstName + lastName;
        if((traineeMap.get(username) != null) || (trainerMap.get(username) != null)){
            return username + getSuffix(username);
        }
        return username;
    }

    private Long getSuffix(String username){
        long s = 0L;
        Set<Long> traineeSuffixSet = traineeMap.keySet().stream().filter(key -> key.startsWith(username))
                .map(key -> key.substring(username.length()))
                .filter(suffix -> !suffix.isEmpty()).map(Long::valueOf).collect(Collectors.toSet());

        Set<Long> trainerSuffixSet = trainerMap.keySet().stream().filter(key -> key.startsWith(username))
                .map(key -> key.substring(username.length()))
                .filter(suffix -> !suffix.isEmpty()).map(Long::valueOf).collect(Collectors.toSet());

        traineeSuffixSet.addAll(trainerSuffixSet);

        if(!traineeSuffixSet.isEmpty()){
            s = Collections.max(traineeSuffixSet) + 1;

        }

        return s;
    }


}
