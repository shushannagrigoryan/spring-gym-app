package org.example.facade;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.exceptions.GymIllegalIdException;
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
        trainerService.createTrainer(trainerEntity);
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

    //    /**
    //     * Updates the trainer with the given id in the facade layer.
    //     *
    //     * @param id         id of the trainer
    //     * @param trainerDto the new trainer data to update with
    //     */
    //    public void updateTrainerById(Long id, TrainerDto trainerDto) {
    //        log.info("Request to update trainer by id");
    //        try {
    //            trainerService.updateTrainerById(id, trainerMapper.dtoToEntity(trainerDto));
    //            log.info("Successfully updated trainer by id");
    //        } catch (GymIllegalIdException exception) {
    //            log.error(exception.getMessage(), exception);
    //        }
    //
    //    }
}
