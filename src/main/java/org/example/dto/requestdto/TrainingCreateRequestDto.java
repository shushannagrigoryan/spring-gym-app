package org.example.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @NotBlank(message = "Trainee username is required.")
    private String traineeUsername;
    @NotBlank(message = "Trainer username is required.")
    private String trainerUsername;
    @NotBlank(message = "Training name is required.")
    private String trainingName;
    @NotNull(message = "Training date is required.")
    private LocalDateTime trainingDate;
    @NotNull(message = "Training duration is required.")
    private BigDecimal trainingDuration;

}
