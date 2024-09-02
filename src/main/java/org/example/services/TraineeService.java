package org.example.services;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.SaveDataToFile;
import org.example.ValidatePassword;
import org.example.model.Trainee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.OptionalLong;

@Service
public class TraineeService {
    @Autowired
    private Map<String, Trainee> traineeStorage;



    @Value("${trainee.storage}")
    private String traineeStorageFile;

    private UserService userService;
    private SaveDataToFile saveDataToFile;

    @Autowired
    public void setDependencies(UserService userService, SaveDataToFile saveDataToFile){
        this.userService = userService;
        this.saveDataToFile = saveDataToFile;
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
//        writeMapToFile();
        saveDataToFile.writeMapToFile("Trainee");
    }

//    private void writeMapToFile() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.findAndRegisterModules();
//        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
//        try (FileWriter fileWriter = new FileWriter(traineeStorageFile)) {
//            fileWriter.write(writer.writeValueAsString(traineeStorage));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


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
