package org.example.services;

import org.example.ValidatePassword;
import org.example.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.OptionalLong;

@Service
public class TrainerService {

    @Autowired
    private Map<String, Trainer> trainerMap;

    private UserService userService;

    @Autowired
    private void setDependencies(UserService userService){
        this.userService = userService;
    }
    public Trainer getTrainer(String username) throws Exception {
        Trainer trainer = trainerMap.get(username);
        if (trainer == null){
            throw new Exception("No trainer with the username: " + username);
        }
        return trainer;
    }

    public void createTrainer(String firstName, String lastName, String password,String specialization){
        Trainer trainer = new Trainer(firstName, lastName, specialization);
        String username = userService.generateUsername(firstName, lastName);
        trainer.setUserName(username);

        if (ValidatePassword.isValidPassword(password)){
            trainer.setPassword(password);
        }
        else{
            throw new IllegalArgumentException("Invalid password");
        }
        trainer.setId(generateId());
        trainerMap.put(username,trainer);

        System.out.println(trainerMap);
    }

    private Long generateId(){
        OptionalLong lastId = trainerMap.values().stream()
                .mapToLong(Trainer::getUserID)
                .max();
        if(lastId.isPresent()){
            return lastId.getAsLong() + 1;
        }
        else{
            return 0L;
        }
    }
}
