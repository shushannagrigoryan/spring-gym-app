package org.example.services;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.TraineeDao;
import org.example.dao.UserDao;
import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.exceptions.GymIllegalIdException;
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

    /**
     * Setting the dependencies for the TraineeService bean.
     */
    @Autowired
    public void setDependencies(UserDao userDao, TraineeDao traineeDao,
                                SaveDataToFile saveDataToFile,
                                TraineeMapper traineeMapper) {
        this.userDao = userDao;
        this.traineeDao = traineeDao;
        this.saveDataToFile = saveDataToFile;
        this.traineeMapper = traineeMapper;
    }

    /**
     * Creates a new trainee in the service layer.
     *
     * @param traineeEntity the new {@code TraineeEntity}
     */
    public void createTrainee(TraineeEntity traineeEntity) {
        log.debug("Creating trainee: {}", traineeEntity);
        String username = userDao.generateUsername(traineeEntity.getFirstName(), traineeEntity.getLastName());
        traineeEntity.setUsername(username);
        traineeEntity.setPassword(userDao.generatePassword());
        traineeDao.createTrainee(traineeEntity);
        saveDataToFile.writeMapToFile("Trainee");
        log.debug("Successfully created trainee: {}", traineeEntity);
    }

    /**
     * Gets trainee by username.
     * If no trainee is found returns null.
     *
     * @param username username of the trainee
     * @return the {@code TraineeDto}
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
        traineeDao.deleteTraineeById(id);
        saveDataToFile.writeMapToFile("Trainee");
        log.debug("Successfully deleted trainee by id: {}", id);
    }

    /**
     * Updates trainee by id.
     *
     * @param id            id of the trainee
     * @param traineeEntity {@code TraineeEntity} to update with
     */
    public void updateTraineeById(Long id, TraineeEntity traineeEntity) {
        log.debug("Updating trainee by id: {}", id);
        Optional<TraineeEntity> trainee = traineeDao.getTraineeById(id);

        if (trainee.isEmpty()) {
            log.debug("No trainee with id: {}", id);
            throw new GymIllegalIdException(String.format("No trainee with id: %d", id));
        }

        TraineeEntity traineeToUpdate = trainee.get();

        String updatedFirstName = traineeEntity.getFirstName();
        String updatedLastName = traineeEntity.getLastName();
        String firstName = traineeToUpdate.getFirstName();
        String lastName = traineeToUpdate.getLastName();

        if (!((firstName.equals(updatedFirstName)) && (lastName.equals(updatedLastName)))) {
            String username = userDao
                    .generateUsername(updatedFirstName, updatedLastName);
            traineeToUpdate.setUsername(username);
        }

        traineeToUpdate.setFirstName(updatedFirstName);
        traineeToUpdate.setLastName(updatedLastName);
        traineeToUpdate.setDateOfBirth(traineeEntity.getDateOfBirth());
        traineeToUpdate.setAddress(traineeEntity.getAddress());
        traineeToUpdate.setActive(traineeEntity.isActive());

        traineeDao.updateTraineeById(id, traineeToUpdate);
        log.debug("Successfully updated trainee with id: {}", id);
        saveDataToFile.writeMapToFile("Trainee");
    }
}
