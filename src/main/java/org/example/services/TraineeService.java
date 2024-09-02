package org.example.services;

import org.example.SaveDataToFile;
import org.example.ValidatePassword;
import org.example.model.Trainee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.OptionalLong;

@Service
public class TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    @Autowired
    private Map<String, Trainee> traineeStorage;

    private UserService userService;
    private SaveDataToFile saveDataToFile;

    @Autowired
    public void setDependencies(UserService userService, SaveDataToFile saveDataToFile){
        this.userService = userService;
        this.saveDataToFile = saveDataToFile;
    }

    public void createTrainee(String firstName, String lastName, String password,
                              LocalDate dateOfBirth, String address){
        logger.debug("Creating trainee with firstName: {}, lastName: {}",
                firstName, lastName);
        Trainee trainee = new Trainee(firstName, lastName, dateOfBirth, address);
        String username = userService.generateUsername(firstName, lastName);
        trainee.setUserName(username);

        if (ValidatePassword.isValidPassword(password)){
            trainee.setPassword(password);
        }
        else{
            logger.debug("Invalid password for trainee");
            throw new IllegalArgumentException("Invalid password");
        }
        trainee.setId(generateId());
        traineeStorage.put(username,trainee);
        logger.debug("Created a new trainee with username: "+ username);
        saveDataToFile.writeMapToFile("Trainee");
    }

    private Long generateId(){
        OptionalLong lastId = traineeStorage.values().stream()
                .mapToLong(Trainee::getUserId)
                .max();
        if(lastId.isPresent()){
            return lastId.getAsLong() + 1;
        }
        else{
            return 0L;
        }
    }

    public Trainee getTrainee(String username) throws Exception {
        Trainee trainee = traineeStorage.get(username);
        if (trainee == null){
            throw new Exception("No trainee with the username: " + username);
        }
        logger.debug("Getting trainee with username: " + username);
        return trainee;
    }

    public void deleteTrainee(String username){
        if(!traineeStorage.containsKey(username)){
            logger.debug("Can't delete trainee: No trainee with username: " + username);
            throw new IllegalArgumentException("No trainee with username: " + username);
        }
        else{
            traineeStorage.remove(username);
        }
        logger.debug("Deleted trainee with username: " + username);
    }

}
