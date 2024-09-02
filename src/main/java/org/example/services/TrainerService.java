package org.example.services;

import org.example.SaveDataToFile;
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
    private SaveDataToFile saveDataToFile;

    @Autowired
    private void setDependencies(UserService userService, SaveDataToFile saveDataToFile){
        this.userService = userService;
        this.saveDataToFile = saveDataToFile;
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

        saveDataToFile.writeMapToFile("Trainer");
    }

    private Long generateId(){
        OptionalLong lastId = trainerMap.values().stream()
                .mapToLong(Trainer::getUserId)
                .max();
        if(lastId.isPresent()){
            return lastId.getAsLong() + 1;
        }
        else{
            return 0L;
        }
    }
}
