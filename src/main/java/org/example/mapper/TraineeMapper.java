package org.example.mapper;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeCreateDto;
import org.example.dto.TraineeDto;
import org.example.dto.TraineeProfileResponseDto;
import org.example.dto.TraineeProfileTrainerResponseDto;
import org.example.dto.TraineeResponseDto;
import org.example.entity.TraineeEntity;
import org.example.entity.TrainingEntity;
import org.example.entity.UserEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TraineeMapper {

    private final TrainerMapper trainerMapper;

    /**Setting dependencies. */
    public TraineeMapper(TrainerMapper trainerMapper) {
        this.trainerMapper = trainerMapper;
    }

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

    /** Maps trainee entity to trainee profile response dto. */
    public TraineeProfileResponseDto entityToProfileDto(TraineeEntity traineeEntity) {
        if (traineeEntity == null) {
            return null;
        }
        TraineeProfileResponseDto traineeProfile = new TraineeProfileResponseDto();
        traineeProfile.setFirstName(traineeEntity.getUser().getFirstName());
        traineeProfile.setLastName(traineeEntity.getUser().getLastName());
        traineeProfile.setDateOfBirth(traineeEntity.getDateOfBirth());
        traineeProfile.setAddress(traineeEntity.getAddress());
        traineeProfile.setActive(traineeProfile.isActive());
        System.out.println("LAZY INITIALIZATION");
        List<TrainingEntity> trainingEntityList = traineeEntity.getTrainings();
        List<TraineeProfileTrainerResponseDto> trainers = trainingEntityList
                .stream()
                .map(x -> trainerMapper.entityToTraineeTrainerResponseDto(x.getTrainer()))
                .toList();

        traineeProfile.setTrainers(trainers);
        return traineeProfile;
    }


}
