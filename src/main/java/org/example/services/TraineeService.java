package org.example.services;

import org.example.MapDataClass;
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
    private MapDataClass storageMap;

    private UserService userService;

    @Autowired
    public void setDependencies(UserService userService){
        this.userService = userService;
    }

    public void createTrainee(String firstName, String lastName, String password,
                              LocalDate dateOfBirth, String address){

        Map<String, Trainee> traineeMap = storageMap.getTraineeMap();
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
        traineeMap.put(username,trainee);
        System.out.println(traineeMap);
        System.out.println(traineeMap.keySet());
    }

    private Long generateId(){
        OptionalLong lastId = storageMap.getTraineeMap().values().stream()
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
        Trainee trainee = storageMap.getTraineeMap().get(username);
        if (trainee == null){
            throw new Exception("No trainee with the username: " + username);
        }
        return trainee;
    }

    public void deleteTrainee(String username){
        Map<String, Trainee> traineeMap = storageMap.getTraineeMap();
        if(!traineeMap.containsKey(username)){
            throw new IllegalArgumentException("No trainee with username: " + username);
        }
        else{
            traineeMap.remove(username);
        }
    }





}
