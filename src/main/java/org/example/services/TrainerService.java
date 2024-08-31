package org.example.services;

import org.example.MapDataClass;
import org.example.ValidatePassword;
import org.example.model.Trainee;
import org.example.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.OptionalLong;

@Service
public class TrainerService {
    @Autowired
    private MapDataClass storageMap;

    private UserService userService;

    @Autowired
    private void setDependencies(UserService userService){
        this.userService = userService;
    }
    public Trainer getTrainer(String username) throws Exception {
        Trainer trainer = storageMap.getTrainerMap().get(username);
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
        storageMap.getTrainerMap().put(username,trainer);

        System.out.println(storageMap.getTrainerMap());
    }

    private Long generateId(){
        OptionalLong lastId = storageMap.getTrainerMap().values().stream()
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
