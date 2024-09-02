package org.example.services;

import org.example.SaveDataToFile;
import org.example.ValidatePassword;
import org.example.dao.TraineeDao;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.exceptions.IllegalIdException;
import org.example.exceptions.IllegalPasswordException;
import org.example.exceptions.IllegalUsernameException;
import org.example.mapper.TraineeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TraineeService {
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    @Autowired
    private TraineeDao traineeDao;

    private UserService userService;
    private SaveDataToFile saveDataToFile;
    private TraineeMapper traineeMapper;

    @Autowired
    public void setDependencies(UserService userService, SaveDataToFile saveDataToFile, TraineeMapper traineeMapper){
        this.userService = userService;
        this.saveDataToFile = saveDataToFile;
        this.traineeMapper = traineeMapper;
    }

    public void createTrainee(TraineeEntity traineeEntity){
        logger.debug("Creating trainee with firstName: {}, lastName: {}",
                traineeEntity.getFirstName(), traineeEntity.getLastName());

        if (!ValidatePassword.isValidPassword(traineeEntity.getPassword())){
            logger.debug("Invalid password for trainee");
            throw new IllegalPasswordException(traineeEntity.getPassword());
        }

        String username = userService.generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName());
        traineeEntity.setUsername(username);
        traineeDao.createTrainee(traineeEntity);
        logger.debug("Created a new trainee with username: "+ username);
        saveDataToFile.writeMapToFile("Trainee");
    }

    public TraineeDto getTraineeByUsername(String username){
        Optional<TraineeEntity> trainee = traineeDao.getTraineeByUsername(username);
        if (!trainee.isPresent()){
            logger.debug("No trainee with the username: " + username);
            throw new IllegalUsernameException(username);
        }
        logger.debug("Getting trainee with username: " + username);
        return traineeMapper.entityToDto(trainee.get());
    }

    public TraineeDto getTraineeById(Long id){
        Optional<TraineeEntity> trainee = traineeDao.getTraineeById(id);
        if (!trainee.isPresent()){
            throw new IllegalIdException(id);
        }
        logger.debug("Getting trainee with username: " + id);
        return traineeMapper.entityToDto(trainee.get());
    }

    public void deleteTraineeById(Long id) {
        traineeDao.deleteTraineeById(id);
        saveDataToFile.writeMapToFile("Trainee");
    }

    public void updateTraineeById(Long id, TraineeEntity traineeEntity){
        if (!ValidatePassword.isValidPassword(traineeEntity.getPassword())){
            logger.debug("Invalid password for trainee");
            throw new IllegalPasswordException(traineeEntity.getPassword());
        }

        String username = userService.generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName());
        traineeEntity.setUsername(username);
        traineeEntity.setUserId(id);
        traineeDao.updateTraineeById(id, traineeEntity);
        logger.debug("Updated trainee with id: " + id);
        saveDataToFile.writeMapToFile("Trainee");
    }
}
