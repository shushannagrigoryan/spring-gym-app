package org.example.facade;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.exceptions.GymDataUpdateException;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.exceptions.GymIllegalIdException;
import org.example.exceptions.GymIllegalStateException;
import org.example.mapper.TrainerMapper;
import org.example.services.TrainerService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainerFacade {
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;

    public TrainerFacade(TrainerService trainerService,
                         TrainerMapper trainerMapper) {
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
    }

    /**
     * Creates a new Trainer in the facade layer.
     *
     * @param trainerDto {@code TrainerDto} to create the {@code TrainerEntity}
     */
    public void createTrainer(TrainerDto trainerDto) {
        log.info("Request to create trainer");
        TrainerEntity trainerEntity = trainerMapper.dtoToEntity(trainerDto);
        try {
            trainerService.createTrainer(trainerEntity);
        } catch (GymIllegalIdException e) {
            log.error("Error while creating trainer: {}", trainerEntity, e);
        }

        log.info("Successfully created trainer");
    }

    /**
     * Gets the trainer by id.
     *
     * @param id the id of the trainer
     * @return the TrainerDto
     */
    public TrainerDto getTrainerById(Long id) {
        log.info("Request to retrieve trainer by id");
        TrainerDto trainerDto = null;
        try {
            trainerDto = trainerService.getTrainerById(id);
            log.info("Successfully retrieved trainer by id {}", id);
        } catch (GymIllegalIdException exception) {
            log.error("No trainer with id: {}", id, exception);
        }
        return trainerDto;
    }


    /**
     * Gets trainer by username.
     *
     * @param username username of the trainer
     * @return {@code TrainerDto}
     */
    public TrainerDto getTrainerByUsername(String username) {
        log.info("Request to retrieve trainer by username");
        TrainerDto trainerDto = null;
        try {
            trainerDto = trainerService.getTrainerByUsername(username);
            log.info("Successfully retrieved trainer by username");
        } catch (GymEntityNotFoundException exception) {
            log.error("No trainer with username: {}", username, exception);
        }
        return trainerDto;
    }

    /**
     * Changes trainer password.
     *
     * @param username username of the trainer
     * @param password password of the trainer
     */

    public void changeTrainerPassword(String username, String password) {
        log.info("Request to change trainer password.");
        try {
            trainerService.changeTrainerPassword(username, password);
        } catch (GymIllegalArgumentException | GymDataUpdateException e) {
            log.error("Exception while changing password", e);
        }
    }

    /**
     * Updates Trainer by id.
     *
     * @param id         id of the trainer to update
     * @param trainerDto new trainer data to update with
     */
    public void updateTrainerById(Long id, TrainerDto trainerDto) {
        log.info("Request to update trainer by id");
        try {
            trainerService.updateTrainerById(id, trainerMapper.dtoToEntity(trainerDto));
            log.info("Successfully updated trainer by id");
        } catch (GymIllegalIdException | GymDataUpdateException exception) {
            log.error(exception.getMessage(), exception);
        }

    }

    /**
     * Activates trainer.
     *
     * @param id id of the Trainer
     */
    public void activateTrainer(Long id) {
        try {
            trainerService.activateTrainer(id);
        } catch (GymIllegalIdException | GymDataUpdateException | GymIllegalStateException exception) {
            log.error("Exception while activating trainer with id: {}", id, exception);
        }
    }

    /**
     * Deactivates trainer.
     *
     * @param id id of the Trainer
     */
    public void deactivateTrainer(Long id) {
        try {
            trainerService.deactivateTrainer(id);
        } catch (GymIllegalIdException | GymDataUpdateException | GymIllegalStateException exception) {
            log.error("Exception while deactivating trainer with id: {}", id, exception);
        }
    }
}
