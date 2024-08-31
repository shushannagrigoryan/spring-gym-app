package org.example.services;

import org.example.ValidatePassword;
import org.example.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.OptionalLong;

@Service
public class TraineeService {
    @Autowired
    private Map<String, Trainee> traineeStorage;

    private UserService userService;

    @Autowired
    public void setDependencies(UserService userService){
        this.userService = userService;
    }

    public void createTrainee(String firstName, String lastName, String password,
                              LocalDate dateOfBirth, String address){
        Trainee trainee = new Trainee(firstName, lastName, dateOfBirth, address);
        String username = userService.generateUsername(firstName, lastName);
        trainee.setUserName(username);

        if (ValidatePassword.isValidPassword(password)){
            trainee.setPassword(password);
        }
        else{
            throw new IllegalArgumentException("Invalid password");
        }

        trainee.setId(generateId());
        traineeStorage.put(username,trainee);
        System.out.println(traineeStorage);
        System.out.println(traineeStorage.keySet());
    }

    private Long generateId(){
        OptionalLong lastId = traineeStorage.values().stream()
                .mapToLong(Trainee::getUserID)
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
        return trainee;
    }

    public void deleteTrainee(String username){
        if(!traineeStorage.containsKey(username)){
            throw new IllegalArgumentException("No trainee with username: " + username);
        }
        else{
            traineeStorage.remove(username);
        }
    }
}
