package org.example.services;

import org.example.SaveDataToFile;
import org.example.ValidatePassword;
import org.example.model.Trainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.OptionalLong;

@Service
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);


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
        logger.debug("Getting trainee with username: " + username);
        return trainer;
    }

    public void createTrainer(String firstName, String lastName, String password,String specialization){
        logger.debug("Creating trainer with firstName: {}, lastName: {}",
                firstName, lastName);
        Trainer trainer = new Trainer(firstName, lastName, specialization);
        String username = userService.generateUsername(firstName, lastName);
        trainer.setUserName(username);

        if (ValidatePassword.isValidPassword(password)){
            trainer.setPassword(password);
        }
        else{
            logger.debug("Invalid password for trainer");
            throw new IllegalArgumentException("Invalid password");
        }
        trainer.setId(generateId());
        trainerMap.put(username,trainer);
        logger.debug("Created new trainer with username: "+ username);
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
