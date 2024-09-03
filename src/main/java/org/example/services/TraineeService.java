package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.SaveDataToFile;
import org.example.ValidatePassword;
import org.example.dao.TraineeDao;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.exceptions.IllegalIdException;
import org.example.exceptions.IllegalPasswordException;
import org.example.exceptions.IllegalUsernameException;
import org.example.mapper.TraineeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TraineeService {

    @Autowired
    private TraineeDao traineeDao;

    private UserService userService;
    private SaveDataToFile saveDataToFile;
    private TraineeMapper traineeMapper;
    private ValidatePassword validatePassword;

    @Autowired
    public void setDependencies(UserService userService, SaveDataToFile saveDataToFile,
                                TraineeMapper traineeMapper, ValidatePassword validatePassword){
        this.userService = userService;
        this.saveDataToFile = saveDataToFile;
        this.traineeMapper = traineeMapper;
        this.validatePassword = validatePassword;
    }

    public void createTrainee(TraineeEntity traineeEntity){
        log.debug("Creating trainee: {}", traineeEntity);

        if (validatePassword.passwordNotValid(traineeEntity.getPassword())){
            log.debug("Invalid password for trainee");
            throw new IllegalPasswordException(traineeEntity.getPassword());
        }
        System.out.println(traineeEntity);

        String username = userService.generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName());
        traineeEntity.setUsername(username);
        traineeDao.createTrainee(traineeEntity);
        saveDataToFile.writeMapToFile("Trainee");
        log.debug("Successfully created trainee: {}", traineeEntity);
    }

    public TraineeDto getTraineeByUsername(String username){
        log.debug("Retrieving trainee by username: {}", username);
        Optional<TraineeEntity> trainee = traineeDao.getTraineeByUsername(username);
        if (!trainee.isPresent()){
            log.debug("No trainee with the username: " + username);
            throw new IllegalUsernameException(username);
        }
        log.debug("Successfully retrieved trainee by username: {}", username);
        return traineeMapper.entityToDto(trainee.get());
    }

    public TraineeDto getTraineeById(Long id){
        log.debug("Retrieving trainee by id: {}", id);
        Optional<TraineeEntity> trainee = traineeDao.getTraineeById(id);
        if (!trainee.isPresent()){
            throw new IllegalIdException("No trainee with id: " + id);
        }
        log.debug("Successfully retrieved trainee by id: {}", id);
        return traineeMapper.entityToDto(trainee.get());
    }

    public void deleteTraineeById(Long id) {
        log.debug("Deleting trainee by id: {}", id);
        try{
            traineeDao.deleteTraineeById(id);
            saveDataToFile.writeMapToFile("Trainee");
            log.debug("Successfully deleted trainee by id: {}", id);
        }catch (IllegalIdException exception){
            log.debug("Failed to delete trainee by id: {}", id);
            throw exception;
        }
    }

    public void updateTraineeById(Long id, TraineeEntity traineeEntity){
        log.debug("Updating trainee by id: {}", id);

        if (validatePassword.passwordNotValid(traineeEntity.getPassword())){
            log.debug("Invalid password for trainee");
            throw new IllegalPasswordException(traineeEntity.getPassword());
        }

        String username = userService.generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName());
        traineeEntity.setUsername(username);
        traineeEntity.setUserId(id);
        traineeDao.updateTraineeById(id, traineeEntity);
        log.debug("Successfully updated trainee with id: " + id);
        saveDataToFile.writeMapToFile("Trainee");
    }
}
