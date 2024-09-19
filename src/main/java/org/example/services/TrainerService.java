package org.example.services;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.TrainerAuth;
import org.example.dao.TrainerDao;
import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalIdException;
import org.example.mapper.TrainerMapper;
import org.example.password.PasswordGeneration;
import org.example.username.UsernameGenerator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TrainerService {
    private final TrainerDao trainerDao;
    private final TrainerMapper trainerMapper;
    private final UsernameGenerator usernameGenerator;
    private final PasswordGeneration passwordGeneration;
    private final TrainerAuth trainerAuth;

    /**
     * Injecting dependencies using constructor.
     */
    public TrainerService(TrainerMapper trainerMapper,
                          TrainerDao trainerDao,
                          UsernameGenerator usernameGenerator,
                          PasswordGeneration passwordGeneration,
                          TrainerAuth trainerAuth) {
        this.trainerMapper = trainerMapper;
        this.trainerDao = trainerDao;
        this.usernameGenerator = usernameGenerator;
        this.passwordGeneration = passwordGeneration;
        this.trainerAuth = trainerAuth;
    }

    /**
     * Creates Trainer in the Service layer.
     *
     * @param trainerEntity {@code TrainerEntity} to create
     */
    public void createTrainer(TrainerEntity trainerEntity) {
        log.debug("Creating trainer: {}", trainerEntity);
        String username = usernameGenerator.generateUsername(
                trainerEntity.getUser().getFirstName(),
                trainerEntity.getUser().getLastName());
        trainerEntity.getUser().setUsername(username);
        trainerEntity.getUser().setPassword(passwordGeneration.generatePassword());
        trainerDao.createTrainer(trainerEntity);
        log.debug("Successfully created a new trainer with username: {}", username);
    }

    /**
     * Gets trainer by username.
     * If no trainer is found returns null.
     *
     * @param username username of the trainer
     * @return the {@code TrainerDto}
     */
    public TrainerDto getTrainerByUsername(String username) {
        log.debug("Retrieving trainer by username: {}", username);
        Optional<TrainerEntity> trainer = trainerDao.getTrainerByUsername(username);
        if (trainer.isEmpty()) {
            log.debug("No trainer with the username: {}", username);
            throw new GymEntityNotFoundException(String.format("Trainer with username %s does not exist.", username));
        }
        log.debug("Successfully retrieved trainer by username: {}", username);
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
        TrainerEntity trainer = trainerDao.getTrainerById(id);
        if (trainer == null) {
            throw new GymIllegalIdException(String.format("No trainer with id: %d", id));
        }
        log.debug("Successfully retrieved trainer with id: {}", id);
        return trainerMapper.entityToDto(trainer);
    }

    /**
     * Changes the password of the trainer by username. Before changing authentication is performed.
     *
     * @param username username of the trainer
     */
    public void changeTrainerPassword(String username, String password) {
        if (trainerAuth.trainerAuth(username, password)) {
            trainerDao.changeTrainerPassword(username, passwordGeneration.generatePassword());
        }
    }

    //    /**
    //     * Updates trainer by id.
    //     *
    //     * @param id            of the trainer
    //     * @param trainerEntity to update with
    //     */
    //    public void updateTrainerById(Long id, TrainerEntity trainerEntity) {
    //        log.debug("Updating trainer by id: {}", id);
    //        Optional<TrainerEntity> trainer = trainerDao.getTrainerById(id);
    //
    //        if (trainer.isEmpty()) {
    //            log.debug("No trainer with id: {}", id);
    //            throw new GymIllegalIdException(String.format("No trainer with id: %d", id));
    //        }
    //
    //        TrainerEntity trainerToUpdate = trainer.get();
    //        String updatedFirstName = trainerEntity.getFirstName();
    //        String updatedLastName = trainerEntity.getLastName();
    //        String firstName = trainerToUpdate.getFirstName();
    //        String lastName = trainerToUpdate.getLastName();
    //
    //        if (!((firstName.equals(updatedFirstName)) && (lastName.equals(updatedLastName)))) {
    //            String username = userDao
    //                    .generateUsername(updatedFirstName, updatedLastName);
    //            trainerToUpdate.setUsername(username);
    //        }
    //
    //        trainerToUpdate.setFirstName(updatedFirstName);
    //        trainerToUpdate.setLastName(updatedLastName);
    //        trainerToUpdate.setSpecialization(trainerEntity.getSpecialization());
    //        trainerToUpdate.setActive(trainerEntity.isActive());
    //
    //        trainerDao.updateTrainerById(id, trainerToUpdate);
    //        log.debug("Successfully updated trainer with id: {}", id);
    //        saveDataToFile.writeMapToFile("Trainer");
    //    }
}
