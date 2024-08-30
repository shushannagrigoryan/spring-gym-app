package org.example.services;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TrainerService {
    @Autowired
    private Map<String, Map<String, Object>> storageMap;
    public Trainer getTrainer(String username) throws Exception {
        Trainer trainer = (Trainer) storageMap.get("Trainer").get(username);
        if (trainer == null){
            throw new Exception("No trainer with the username: " + username);
        }
        return trainer;
    }


}
