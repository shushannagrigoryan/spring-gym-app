package org.example.services;

import org.example.MapDataClass;
import org.example.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainerService {
    @Autowired
    private MapDataClass storageMap;
    public Trainer getTrainer(String username) throws Exception {
        Trainer trainer = storageMap.getTrainerMap().get(username);
        if (trainer == null){
            throw new Exception("No trainer with the username: " + username);
        }
        return trainer;
    }



}
