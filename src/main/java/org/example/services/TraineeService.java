package org.example.services;

import org.example.ValidatePassword;
import org.example.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Service
public class TraineeService {
    @Autowired
    private Map<String, Map<String, Object>> storageMap;

    @Value("${username.suffix}")
    private String usernameSuffixPath;

    public void createTrainee(String firstName, String lastName, String password,
                              LocalDate dateOfBirth, String address){

        Map<String, Object> traineeMap = storageMap.get("Trainee");
        Trainee trainee = new Trainee(firstName, lastName, dateOfBirth, address);
        String username = generateUsername(firstName, lastName);
        trainee.setUserName(username);

        if (ValidatePassword.isValidPassword(password)){
            trainee.setPassword(password);
        }
        else{
            throw new IllegalArgumentException("Invalid password");
        }

        traineeMap.put(username,trainee);
    }

    private String generateUsername(String firstName, String lastName){
        String username = firstName + lastName;
        if(storageMap.get("Trainee").get(username) != null){
            return username + getUsernameSuffix();
        }
        return username;
    }

    private Long getUsernameSuffix(){
        long usernameSuffix = 0L;
        try(FileReader fileReader = new FileReader(usernameSuffixPath);
            BufferedReader bufferedReader = new BufferedReader(fileReader)){
            String fileNumber = bufferedReader.readLine();
            if(fileNumber != null){
                usernameSuffix = Long.parseLong(fileNumber) + 1;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return usernameSuffix;
    }






}
