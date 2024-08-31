package org.example.services;

import org.example.MapDataClass;
import org.example.ValidatePassword;
import org.example.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.Map;

@Service
public class TraineeService {
    @Autowired
    private MapDataClass storageMap;

    @Value("${username.suffix}")
    private String usernameSuffixPath;

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

        traineeMap.put(username,trainee);
        System.out.println(traineeMap);
        System.out.println(traineeMap.keySet());
    }

    public Trainee getTrainee(String username) throws Exception {
        Trainee trainee = storageMap.getTraineeMap().get(username);
        if (trainee == null){
            throw new Exception("No trainee with the username: " + username);
        }
        return trainee;
    }



}
