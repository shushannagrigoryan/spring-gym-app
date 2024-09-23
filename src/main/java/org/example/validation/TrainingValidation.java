package org.example.validation;

import org.example.dto.TrainingDto;
import org.example.exceptions.GymIllegalArgumentException;
import org.springframework.stereotype.Component;

@Component
public class TrainingValidation {
    /** Validates {@code TrainingDto} for required fields. */
    public void validateTraining(TrainingDto trainingDto) {
        if (trainingDto.getTrainingName() == null || trainingDto.getTrainingName().trim().isEmpty()) {
            throw new GymIllegalArgumentException("Training name is required.");
        }
        if (trainingDto.getTrainingDate() == null) {
            throw new GymIllegalArgumentException("Training date is required.");
        }
        if (trainingDto.getTrainingDuration() == null) {
            throw new GymIllegalArgumentException("Training duration is required.");
        }
    }
}
