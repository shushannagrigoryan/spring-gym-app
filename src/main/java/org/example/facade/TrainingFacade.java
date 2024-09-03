package org.example.facade;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TrainingDto;
import org.example.entity.TrainingEntity;
import org.example.exceptions.IllegalIdException;
import org.example.mapper.TrainingMapper;
import org.example.services.TrainingService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainingFacade {
    private final TrainingService trainingService;
    private final TrainingMapper trainingMapper;

    public TrainingFacade(TrainingService trainingService, TrainingMapper trainingMapper) {
        this.trainingService = trainingService;
        this.trainingMapper = trainingMapper;
    }

    public void createTraining(TrainingDto trainingDto){
        log.info("Request to create training");
        TrainingEntity trainingEntity = trainingMapper.dtoToEntity(trainingDto);
        try{
            trainingService.createTraining(trainingEntity);
            log.info("Successfully created training");
        }catch(IllegalIdException exception){
            log.error(exception.getMessage(), exception);
        }
    }

    public TrainingDto getTrainingById(Long id){
        log.info("Request to retrieve training by id");
        TrainingDto trainingDto;
        try{
            trainingDto = trainingService.getTrainingById(id);
            log.info("Successfully retrieved training by id");
        }catch (IllegalIdException exception){
            log.error(exception.getMessage());
            throw exception;
        }
        return trainingDto;
    }
}
