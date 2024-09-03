package org.example.facade;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainerDto;
import org.example.entity.TrainerEntity;
import org.example.exceptions.IllegalIdException;
import org.example.exceptions.IllegalPasswordException;
import org.example.exceptions.IllegalUsernameException;
import org.example.mapper.TrainerMapper;
import org.example.services.TrainerService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainerFacade {
    private final TrainerService trainerService;
    private final TrainerMapper trainerMapper;

    public TrainerFacade(TrainerService trainerService, TrainerMapper trainerMapper){
        this.trainerService = trainerService;
        this.trainerMapper = trainerMapper;
    }

    public void createTrainer(TrainerDto trainerDto){
        log.info("Request to create trainer");
        TrainerEntity trainerEntity = trainerMapper.dtoToEntity(trainerDto);
        try{
            trainerService.createTrainer(trainerEntity);
            log.info("Successfully created trainer");
        }
        catch (IllegalPasswordException exception){
            log.error(exception.getMessage(), exception);
        }
    }

    public TrainerDto getTrainerByUsername(String username){
        log.info("Request to retrieve trainer by username");
        TrainerDto trainerDto;
        try{
            trainerDto = trainerService.getTrainerByUsername(username);
            log.info("Successfully retrieved trainer by username");
        }catch (IllegalUsernameException exception){
            log.error(exception.getMessage());
            throw exception;
        }
        return trainerDto;
    }

    public TrainerDto getTrainerById(Long id){
        log.info("Request to retrieve trainer by id");
        TrainerDto trainerDto;
        try{
            trainerDto = trainerService.getTrainerById(id);
            log.info("Successfully retrieved trainer by id");
        }catch (IllegalIdException exception){
            log.error(exception.getMessage());
            throw exception;
        }
        return trainerDto;
    }

    public void updateTrainerById(Long id, TrainerDto trainerDto){
        log.info("Request to update trainer by id");
        try{
            trainerService.updateTrainerById(id, trainerMapper.dtoToEntity(trainerDto));
            log.info("Successfully updated trainer by id");
        }catch(IllegalPasswordException | IllegalIdException exception){
            log.error(exception.getMessage(), exception);
        }

    }
}
