package org.example.services;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.TraineeDao;
import org.example.dao.UserDao;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalPasswordException;
import org.example.mapper.TraineeMapper;
import org.example.storage.SaveDataToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TraineeService {
    private TraineeDao traineeDao;
    private UserDao userDao;
    private SaveDataToFile saveDataToFile;
    private TraineeMapper traineeMapper;
    private ValidatePassword validatePassword;

    /**
     * Setting the dependencies for the TraineeService bean.
     */
    @Autowired
    public void setDependencies(UserDao userDao, TraineeDao traineeDao,
                                SaveDataToFile saveDataToFile,
                                TraineeMapper traineeMapper,
                                ValidatePassword validatePassword) {
        this.userDao = userDao;
        this.traineeDao = traineeDao;
        this.saveDataToFile = saveDataToFile;
        this.traineeMapper = traineeMapper;
        this.validatePassword = validatePassword;
    }

    /**
     * Creates a new trainee in the service layer.
     * If password is not valid throws an {@code IllegalPasswordException}.
     *
     * @param traineeEntity the new {@code TraineeEntity}
     */
    public void createTrainee(TraineeEntity traineeEntity) {
        log.debug("Creating trainee: {}", traineeEntity);

        if (validatePassword.passwordNotValid(traineeEntity.getPassword())) {
            log.debug("Invalid password for trainee");
            throw new GymIllegalPasswordException(traineeEntity.getPassword());
        }

        String username = userDao.generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName());
        traineeEntity.setUsername(username);
        traineeDao.createTrainee(traineeEntity);
        saveDataToFile.writeMapToFile("Trainee");
        log.debug("Successfully created trainee: {}", traineeEntity);
    }

    /**
     * Gets trainee by username.
     * If no trainee is found returns null.
     *
     * @param username username of the trainee
     * @return the {@code TrainerDto}
     */
    public TraineeDto getTraineeByUsername(String username) {
        log.debug("Retrieving trainee by username: {}", username);
        Optional<TraineeEntity> trainee = traineeDao.getTraineeByUsername(username);
        if (trainee.isEmpty()) {
            log.debug("No trainee with the username: {}", username);
            return null;
        }
        log.debug("Successfully retrieved trainee by username: {}", username);
        return traineeMapper.entityToDto(trainee.get());
    }

    /**
     * Gets the trainee by id in the service layer.
     * If no trainee was found throws an {@code GymIllegalIdException}
     *
     * @param id id of the trainee to get
     * @return the {@code TraineeDto}
     */
    public TraineeDto getTraineeById(Long id) {
        log.debug("Retrieving trainee by id: {}", id);
        Optional<TraineeEntity> trainee = traineeDao.getTraineeById(id);
        if (trainee.isEmpty()) {
            throw new GymIllegalIdException(String.format("No trainee with id: %d", id));
        }
        log.debug("Successfully retrieved trainee by id: {}", id);
        return traineeMapper.entityToDto(trainee.get());
    }

    /**
     * Deletes a trainee by id in the service layer.
     * If there is no trainee with the given id throws an {@code GymIllegalIdException}.
     *
     * @param id the id of the trainee
     */
    public void deleteTraineeById(Long id) {
        log.debug("Deleting trainee by id: {}", id);
        try {
            traineeDao.deleteTraineeById(id);
            saveDataToFile.writeMapToFile("Trainee");
            log.debug("Successfully deleted trainee by id: {}", id);
        } catch (GymIllegalIdException exception) {
            log.debug("Failed to delete trainee by id: {}", id);
            throw exception;
        }
    }

    /**
     * Updates trainee by id.
     * If password is not valid throws an {@code IllegalPasswordException}.
     *
     * @param id            id of the trainee
     * @param traineeEntity {@code TraineeEntity} to update with
     */
    public void updateTraineeById(Long id, TraineeEntity traineeEntity) {
        log.debug("Updating trainee by id: {}", id);

        if (validatePassword.passwordNotValid(traineeEntity.getPassword())) {
            log.debug("Invalid password for trainee");
            throw new GymIllegalPasswordException(traineeEntity.getPassword());
        }

        String username = userDao
                .generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName());
        traineeEntity.setUsername(username);
        traineeEntity.setUserId(id);
        traineeDao.updateTraineeById(id, traineeEntity);
        log.debug("Successfully updated trainee with id: {}", id);
        saveDataToFile.writeMapToFile("Trainee");


    }
}
