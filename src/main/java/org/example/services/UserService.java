package org.example.services;

import org.example.model.Trainee;
import org.example.model.Trainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private Map<String, Trainee> traineeMap;
    @Autowired
    private Map<String, Trainer> trainerMap;

    @Value("${username.suffix}")
    private String usernameSuffixPath;

    String generateUsername(String firstName, String lastName){
        String username = firstName + lastName;

        if((traineeMap.get(username) != null) || (trainerMap.get(username) != null)){
            System.out.println("Username: " + username + " taken");
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


}
