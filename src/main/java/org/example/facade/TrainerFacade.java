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
        TrainerEntity trainerEntity = trainerMapper.dtoToEntity(trainerDto);
        try{
            trainerService.createTrainer(trainerEntity);
        }
        catch (IllegalPasswordException exception){
            log.error(exception.getMessage(), exception);
        }
    }

    public TrainerDto getTrainerByUsername(String username){
        TrainerDto trainerDto;
        try{
            trainerDto = trainerService.getTrainerByUsername(username);
        }catch (IllegalUsernameException exception){
            log.error(exception.getMessage());
            throw exception;
        }
        return trainerDto;
    }

    public TrainerDto getTrainerById(Long id){
        TrainerDto trainerDto;
        try{
            trainerDto = trainerService.getTrainerById(id);
        }catch (IllegalIdException exception){
            log.error(exception.getMessage());
            throw exception;
        }
        return trainerDto;
    }

    public void updateTrainerById(Long id, TrainerDto trainerDto){
        try{
            trainerService.updateTrainerById(id, trainerMapper.dtoToEntity(trainerDto));
        }catch(IllegalPasswordException | IllegalIdException exception){
            log.error(exception.getMessage(), exception);
        }

    }
}
