package org.example.facade;

import org.example.dto.TraineeDto;
import org.example.entity.TraineeEntity;
import org.example.exceptions.IllegalIdException;
import org.example.exceptions.IllegalPasswordException;
import org.example.exceptions.IllegalUsernameException;
import org.example.mapper.TraineeMapper;
import org.example.services.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TraineeFacade {
    private static final Logger logger = LoggerFactory.getLogger(TraineeFacade.class);
    private final TraineeService traineeService;
    private final TraineeMapper traineeMapper;

    public TraineeFacade(TraineeService traineeService, TraineeMapper traineeMapper){
        this.traineeService = traineeService;
        this.traineeMapper = traineeMapper;
    }

    public void createTrainee(TraineeDto traineeDto){
        TraineeEntity traineeEntity = traineeMapper.dtoToEntity(traineeDto);
        try{
            traineeService.createTrainee(traineeEntity);
        }
        catch (IllegalPasswordException exception){
            logger.error(exception.getMessage(), exception);
        }
    }

    public TraineeDto getTraineeByUsername(String username){
        TraineeDto traineeDto;
        try{
            traineeDto = traineeService.getTraineeByUsername(username);
        }catch (IllegalUsernameException exception){
            logger.error(exception.getMessage());
            throw exception;
        }
        return traineeDto;
    }

    public TraineeDto getTraineeById(Long id){
        TraineeDto traineeDto;
        try{
            traineeDto = traineeService.getTraineeById(id);
        }catch (IllegalIdException exception){
            logger.error(exception.getMessage());
            throw exception;
        }
        return traineeDto;
    }

    public void deleteTraineeById(Long id){
        try{
            traineeService.deleteTraineeById(id);
        }catch (IllegalIdException exception){
            logger.error("No trainee with id: {} for deleting",id, exception);
        }

    }

    public void updateTraineeById(Long id, TraineeDto traineeDto){
        try{
            traineeService.updateTraineeById(id, traineeMapper.dtoToEntity(traineeDto));
        }catch(IllegalPasswordException | IllegalIdException exception){
            logger.error(exception.getMessage(), exception);
        }

    }


}
