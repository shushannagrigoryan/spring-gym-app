package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeCreateDto;
import org.example.dto.TraineeDto;
import org.example.dto.TraineeResponseDto;
import org.example.dto.TrainerProfileTraineeResponseDto;
import org.example.entity.TraineeEntity;
import org.example.entity.UserEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TraineeMapper {
    /**
     * Maps a {@code TraineeEntity} to a {@code TraineeDto}.
     */
    public TraineeDto entityToDto(TraineeEntity traineeEntity) {
        log.info("Mapping TraineeEntity {} to TraineeDto", traineeEntity);
        if (traineeEntity == null) {
            return null;
        }

        return new TraineeDto(traineeEntity.getUser().getFirstName(),
                traineeEntity.getUser().getLastName(),
                traineeEntity.getDateOfBirth(), traineeEntity.getAddress());
    }

    /**
     * Maps a {@code TraineeEntity} to a {@code TraineeResponseDto}.
     */
    public TraineeResponseDto entityToResponseDto(TraineeEntity traineeEntity) {
        log.info("Mapping TraineeResponseEntity {} to TraineeDto", traineeEntity);
        if (traineeEntity == null) {
            return null;
        }

        return new TraineeResponseDto(traineeEntity.getUser().getUsername(),
                traineeEntity.getUser().getPassword());
    }

    /**
     * Maps a {@code TraineeDto} to {@code TraineeEntity}.
     */
    public TraineeEntity dtoToEntity(TraineeDto traineeDto) {
        log.info("Mapping TraineeDto {} to TraineeEntity", traineeDto);
        if (traineeDto == null) {
            return null;
        }

        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setUser(new UserEntity(traineeDto.getFirstName(), traineeDto.getLastName()));
        traineeEntity.setDateOfBirth(traineeDto.getDateOfBirth());
        traineeEntity.setAddress(traineeDto.getAddress());

        return traineeEntity;
    }

    /**
     * Maps a {@code TraineeCreateDto} to {@code TraineeEntity}.
     */
    public TraineeEntity dtoToEntity(TraineeCreateDto traineeCreateDto) {
        log.info("Mapping TraineeDto {} to TraineeEntity", traineeCreateDto);
        if (traineeCreateDto == null) {
            return null;
        }

        TraineeEntity traineeEntity = new TraineeEntity();
        traineeEntity.setUser(new UserEntity(traineeCreateDto.getFirstName(), traineeCreateDto.getLastName()));
        traineeEntity.setDateOfBirth(traineeCreateDto.getDateOfBirth());
        traineeEntity.setAddress(traineeCreateDto.getAddress());

        return traineeEntity;
    }

    /**Mapping trainee entity to trainee response dto. */
    public TrainerProfileTraineeResponseDto entityToTrainerTraineeResponseDto(TraineeEntity traineeEntity) {
        if (traineeEntity == null) {
            return null;
        }
        TrainerProfileTraineeResponseDto trainee = new TrainerProfileTraineeResponseDto();
        trainee.setUsername(traineeEntity.getUser().getUsername());
        trainee.setFirstName(traineeEntity.getUser().getFirstName());
        trainee.setLastName(traineeEntity.getUser().getLastName());

        return trainee;
    }


}
