package org.example.validation;

import org.example.dto.TrainerDto;
import org.example.exceptions.GymIllegalArgumentException;
import org.springframework.stereotype.Component;

@Component
public class TrainerValidation {
    /** Validates {@code TrainerDto} for required fields. */
    public void validateTrainer(TrainerDto trainerDto) {
        if (trainerDto.getFirstName() == null || trainerDto.getFirstName().trim().isEmpty()) {
            throw new GymIllegalArgumentException("Trainer firstName is required.");
        }
        if (trainerDto.getLastName() == null || trainerDto.getLastName().trim().isEmpty()) {
            throw new GymIllegalArgumentException("Trainer lastName is required.");
        }
    }
}
