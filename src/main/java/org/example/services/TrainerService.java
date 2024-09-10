package org.example.services;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.dao.TrainerDao;
import org.example.dao.UserDao;
import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.exceptions.GymIllegalIdException;
import org.example.mapper.TrainerMapper;
import org.example.storage.SaveDataToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainerService {
    private TrainerDao trainerDao;
    private UserDao userDao;
    private SaveDataToFile saveDataToFile;
    private TrainerMapper trainerMapper;

    /**
     * Setting dependencies for TrainerService.
     */
    @Autowired
    public void setDependencies(TrainerDao trainerDao,
                                UserDao userDao,
                                SaveDataToFile saveDataToFile,
                                TrainerMapper trainerMapper) {
        this.trainerDao = trainerDao;
        this.userDao = userDao;
        this.saveDataToFile = saveDataToFile;
        this.trainerMapper = trainerMapper;
    }

    /**
     * Creates Trainer in the Service layer.
     *
     * @param trainerEntity {@code TrainerEntity} to create
     */
    public void createTrainer(TrainerEntity trainerEntity) {
        log.debug("Creating trainer: {}", trainerEntity);
        String username = userDao.generateUsername(trainerEntity.getFirstName(),
                trainerEntity.getLastName());
        trainerEntity.setUsername(username);
        trainerEntity.setPassword(userDao.generatePassword());
        trainerDao.createTrainer(trainerEntity);
        log.debug("Successfully created a new trainer with username: {}", username);
        saveDataToFile.writeMapToFile("Trainer");
    }

    /**
     * Gets trainer by username.
     * If no trainer is found return null.
     *
     * @param username username of the trainer
     * @return the {@code TrainerDto}
     */
    public TrainerDto getTrainerByUsername(String username) {
        log.debug("Retrieving trainer by username: {}", username);
        Optional<TrainerEntity> trainer = trainerDao.getTrainerByUsername(username);
        if (trainer.isEmpty()) {
            log.debug("No trainer with the username: {}", username);
            return null;
        }
        log.debug("Successfully retrieved trainer with username: {}", username);
        return trainerMapper.entityToDto(trainer.get());
    }

    /**
     * Gets trainer by id.
     * If there is no trainer with the given id throws an {@code GymIllegalIdException}
     *
     * @param id of the trainer
     * @return the TrainerDao
     */
    public TrainerDto getTrainerById(Long id) {
        log.debug("Retrieving trainer by id: {}", id);
        Optional<TrainerEntity> trainer = trainerDao.getTrainerById(id);
        if (trainer.isEmpty()) {
            throw new GymIllegalIdException(String.format("No trainer with id: %d", id));
        }
        log.debug("Successfully retrieved trainer with id: {}", id);
        return trainerMapper.entityToDto(trainer.get());
    }

    /**
     * Updates trainer by id.
     *
     * @param id            of the trainer
     * @param trainerEntity to update with
     */
    public void updateTrainerById(Long id, TrainerEntity trainerEntity) {
        log.debug("Updating trainer by id: {}", id);
        Optional<TrainerEntity> trainer = trainerDao.getTrainerById(id);

        if (trainer.isEmpty()) {
            log.debug("No trainer with id: {}", id);
            throw new GymIllegalIdException(String.format("No trainer with id: %d", id));
        }

        TrainerEntity trainerToUpdate = trainer.get();
        String updatedFirstName = trainerEntity.getFirstName();
        String updatedLastName = trainerEntity.getLastName();
        String firstName = trainerToUpdate.getFirstName();
        String lastName = trainerToUpdate.getLastName();

        if (!((firstName.equals(updatedFirstName)) && (lastName.equals(updatedLastName)))) {
            String username = userDao
                    .generateUsername(updatedFirstName, updatedLastName);
            trainerToUpdate.setUsername(username);
        }

        trainerToUpdate.setFirstName(updatedFirstName);
        trainerToUpdate.setLastName(updatedLastName);
        trainerToUpdate.setSpecialization(trainerEntity.getSpecialization());
        trainerToUpdate.setActive(trainerEntity.isActive());

        trainerDao.updateTrainerById(id, trainerToUpdate);
        log.debug("Successfully updated trainer with id: {}", id);
        saveDataToFile.writeMapToFile("Trainer");
    }
}
