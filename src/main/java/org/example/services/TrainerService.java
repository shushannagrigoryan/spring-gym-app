package org.example.services;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.SaveDataToFile;
import org.example.ValidatePassword;
import org.example.dao.TrainerDao;
import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.exceptions.IllegalIdException;
import org.example.exceptions.IllegalPasswordException;
import org.example.exceptions.IllegalUsernameException;
import org.example.mapper.TrainerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainerService {
    @Autowired
    private TrainerDao trainerDao;

    private UserService userService;
    private SaveDataToFile saveDataToFile;
    private TrainerMapper trainerMapper;
    private ValidatePassword validatePassword;

    /**
     * Setting dependencies for TrainerService.
     */
    @Autowired
    public void setDependencies(UserService userService, SaveDataToFile saveDataToFile,
                                TrainerMapper trainerMapper, ValidatePassword validatePassword) {
        this.userService = userService;
        this.saveDataToFile = saveDataToFile;
        this.trainerMapper = trainerMapper;
        this.validatePassword = validatePassword;
    }

    /**
     * Creates Trainer in the Service layer.
     */
    public void createTrainer(TrainerEntity trainerEntity) {
        log.debug("Creating trainer: {}", trainerEntity);

        if (validatePassword.passwordNotValid(trainerEntity.getPassword())) {
            log.debug("Failed to created trainer: {} .Invalid password for trainer: {}",
                    trainerEntity, trainerEntity.getPassword());
            throw new IllegalPasswordException(trainerEntity.getPassword());
        }

        String username = userService.generateUsername(trainerEntity.getFirstName(),
                trainerEntity.getLastName());
        trainerEntity.setUsername(username);
        trainerDao.createTrainer(trainerEntity);
        log.debug("Successfully created a new trainer with username: " + username);
        saveDataToFile.writeMapToFile("Trainer");
    }

    /**
     * Gets Trainer by username.
     */
    public TrainerDto getTrainerByUsername(String username) {
        log.debug("Retrieving trainer by username: {}", username);
        Optional<TrainerEntity> trainer = trainerDao.getTrainerByUsername(username);
        if (trainer.isEmpty()) {
            log.debug("No trainer with the username: " + username);
            throw new IllegalUsernameException(username);
        }
        log.debug("Successfully retrieved trainer with username: " + username);
        return trainerMapper.entityToDto(trainer.get());
    }

    /**
     * Gets Trainer by id.
     * If there is no trainer with the given id throws an IllegalIdException
     *
     * @param id of the trainer
     * @return the TrainerDao
     */
    public TrainerDto getTrainerById(Long id) {
        log.debug("Retrieving trainer by id: {}", id);
        Optional<TrainerEntity> trainer = trainerDao.getTrainerById(id);
        if (trainer.isEmpty()) {
            throw new IllegalIdException("No trainer with id: " + id);
        }
        log.debug("Successfully retrieved trainer with id: " + id);
        return trainerMapper.entityToDto(trainer.get());
    }

    /**
     * Updates trainer by id.
     * If new password is not valid throws IllegalPasswordException.
     *
     * @param id            of the trainer
     * @param trainerEntity to update with
     */
    public void updateTrainerById(Long id, TrainerEntity trainerEntity) {
        log.debug("Updating trainer by id: {}", id);
        if (validatePassword.passwordNotValid(trainerEntity.getPassword())) {
            log.debug("Invalid password for trainer");
            throw new IllegalPasswordException(trainerEntity.getPassword());
        }

        String username = userService.generateUsername(trainerEntity.getFirstName(),
                trainerEntity.getLastName());
        trainerEntity.setUsername(username);
        trainerEntity.setUserId(id);
        trainerDao.updateTrainerById(id, trainerEntity);
        log.debug("Successfully updated trainer with id: " + id);
        saveDataToFile.writeMapToFile("Trainer");
    }
}
