package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeDto;
import org.example.dto.requestdto.TraineeCreateRequestDto;
import org.example.dto.requestdto.TraineeUpdateRequestDto;
import org.example.dto.responsedto.TraineeResponseDto;
import org.example.dto.responsedto.TrainerProfileTraineeResponseDto;
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
    public TraineeEntity dtoToEntity(TraineeCreateRequestDto traineeCreateDto) {
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

    /**
     * Mapping trainee entity to trainee response dto.
     */
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

    /**
     * Mapping trainee update dto to trainee entity.
     */
    public TraineeEntity updateDtoToEntity(TraineeUpdateRequestDto traineeDto) {
        if (traineeDto == null) {
            return null;
        }
        TraineeEntity trainee = new TraineeEntity();

        UserEntity user = new UserEntity();
        user.setUsername(traineeDto.getUsername());
        user.setFirstName(traineeDto.getFirstName());
        user.setLastName(traineeDto.getLastName());
        user.setActive(traineeDto.getIsActive());
        trainee.setUser(user);

        trainee.setAddress(traineeDto.getAddress());
        trainee.setDateOfBirth(traineeDto.getDateOfBirth());

        return trainee;
    }


}
