package org.example.mapper;

import lombok.extern.slf4j.Slf4j;
import org.example.dto.TraineeProfileTrainerResponseDto;
import org.example.dto.TrainerCreateDto;
import org.example.dto.TrainerDto;
import org.example.dto.TrainerProfileDto;
import org.example.dto.TrainerResponseDto;
import org.example.dto.TrainerUpdateRequestDto;
import org.example.dto.TrainingTypeResponseDto;
import org.example.entity.TrainerEntity;
import org.example.entity.UserEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainerMapper {
    private final TrainingTypeMapper trainingTypeMapper;

    /** Setting dependencies. */
    public TrainerMapper(TrainingTypeMapper trainingTypeMapper) {
        this.trainingTypeMapper = trainingTypeMapper;
    }

    /**
     * Maps a {@code TrainerEntity} to a {@code TrainerDto}.
     */
    public TrainerDto entityToDto(TrainerEntity trainerEntity) {
        log.info("Mapping TrainerEntity {} to TrainerDto", trainerEntity);
        if (trainerEntity == null) {
            return null;
        }

        return new TrainerDto(trainerEntity.getUser().getFirstName(),
                trainerEntity.getUser().getLastName(),
                trainerEntity.getSpecialization().getId());
    }

    /**
     * Maps a {@code TrainerEntity} to a {@code TrainerDto}.
     */
    public TrainerResponseDto entityToResponseDto(TrainerEntity trainerEntity) {
        log.info("Mapping TrainerEntity {} to TrainerResponseDto", trainerEntity);
        if (trainerEntity == null) {
            return null;
        }

        return new TrainerResponseDto(trainerEntity.getUser().getUsername(),
                trainerEntity.getUser().getPassword());
    }

    /**
     * Maps a {@code TrainerDto} to a {@code TrainerEntity}.
     */
    public TrainerEntity dtoToEntity(TrainerDto trainerDto) {
        log.info("Mapping TrainerDto {} to TrainerEntity", trainerDto);
        if (trainerDto == null) {
            return null;
        }

        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUser(new UserEntity(trainerDto.getFirstName(), trainerDto.getLastName()));

        trainerEntity.setSpecializationId(trainerDto.getSpecialization());

        return trainerEntity;
    }

    /**
     * Maps a {@code TrainerCreateDto} to a {@code TrainerEntity}.
     */
    public TrainerEntity dtoToEntity(TrainerCreateDto trainerCreateDto) {
        log.info("Mapping TrainerCreateDto {} to TrainerEntity", trainerCreateDto);
        if (trainerCreateDto == null) {
            return null;
        }

        TrainerEntity trainerEntity = new TrainerEntity();
        trainerEntity.setUser(new UserEntity(trainerCreateDto.getFirstName(),
                trainerCreateDto.getLastName()));

        trainerEntity.setSpecializationId(trainerCreateDto.getSpecialization());

        return trainerEntity;
    }

    /**Mapping trainer entity to trainer response dto. */
    public TraineeProfileTrainerResponseDto entityToTraineeTrainerResponseDto(TrainerEntity trainerEntity) {
        if (trainerEntity == null) {
            return null;
        }
        TraineeProfileTrainerResponseDto trainer = new TraineeProfileTrainerResponseDto();
        trainer.setUsername(trainerEntity.getUser().getUsername());
        trainer.setFirstName(trainerEntity.getUser().getFirstName());
        trainer.setLastName(trainerEntity.getUser().getLastName());
        TrainingTypeResponseDto trainingType = trainingTypeMapper
                .entityToResponseDto(trainerEntity.getSpecialization());
        trainer.setSpecialization(trainingType);

        return trainer;
    }

    /** Mapping trainer update request dto to trainer entity. */
    public TrainerEntity updateDtoToEntity(TrainerUpdateRequestDto trainer) {
        if (trainer == null) {
            return null;
        }

        TrainerEntity trainerEntity = new TrainerEntity();
        UserEntity user = new UserEntity();
        user.setUsername(trainer.getUsername());
        user.setFirstName(trainer.getFirstName());
        user.setLastName(trainer.getLastName());
        user.setActive(trainer.getIsActive());
        trainerEntity.setUser(user);

        trainerEntity.setSpecializationId(trainer.getSpecialization());
        return trainerEntity;
    }

    /** Mapping trainer entity to trainer profile dto(username, firstName, lastName, specialization). */
    public TrainerProfileDto entityToProfileDto(TrainerEntity trainerEntity) {
        if (trainerEntity == null) {
            return null;
        }

        return new TrainerProfileDto(
                trainerEntity.getUser().getUsername(),
                trainerEntity.getUser().getFirstName(),
                trainerEntity.getUser().getLastName(),
                new TrainingTypeResponseDto(trainerEntity.getSpecialization().getId(),
                        trainerEntity.getSpecialization().getTrainingTypeName()));

    }
}
