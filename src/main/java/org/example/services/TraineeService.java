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

    public void createTrainee(String firstName, String lastName, String password,
                              LocalDate dateOfBirth, String address){

        Map<String, Trainee> traineeMap = storageMap.getTraineeMap();
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
        System.out.println(traineeMap);
    }

    private String generateUsername(String firstName, String lastName){
        String username = firstName + lastName;

        if(storageMap.getTraineeMap().get(username) != null){
            return username + getUsernameSuffix();
        }
        return username;
    }

    private Long getUsernameSuffix(){
        long usernameSuffix = 0L;
        try(FileReader fileReader = new FileReader(usernameSuffixPath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
        ){
            String fileNumber = bufferedReader.readLine();
            System.out.println(fileNumber);
            if(fileNumber != null){
                System.out.println(fileNumber);
                usernameSuffix = Long.parseLong(fileNumber) + 1;
            }
            writeUsernameSuffixToFile(usernameSuffix);
        }catch (IOException e){
            e.printStackTrace();
        }
        return usernameSuffix;
    }

    private void writeUsernameSuffixToFile(long usernameSuffix){
        try(FileWriter fileWriter = new FileWriter(usernameSuffixPath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)){
            bufferedWriter.write(String.valueOf(usernameSuffix));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Trainee getTrainee(String username) throws Exception {
        Trainee trainee = storageMap.getTraineeMap().get(username);
        if (trainee == null){
            throw new Exception("No trainee with the username: " + username);
        }
        return trainee;
    }

}
