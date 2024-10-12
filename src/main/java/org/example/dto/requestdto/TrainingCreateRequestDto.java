package org.example.dto.requestdto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingCreateRequestDto {
    @NotNull(message = "Trainee username is required.")
    private String traineeUsername;
    @NotNull(message = "Trainer username is required.")
    private String trainerUsername;
    @NotNull(message = "Training name is required.")
    private String trainingName;
    @NotNull(message = "Training type is required.")
    private Long trainingType;
    @NotNull(message = "Training date is required.")
    private LocalDate trainingDate;
    @NotNull(message = "Training duration is required.")
    private BigDecimal trainingDuration;

}
