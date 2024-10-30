package org.example.dto.requestdto;

import jakarta.validation.constraints.NotNull;
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
public class TrainerTrainingsFilterRequestDto {
    @NotNull(message = "Trainer Username is required.")
    private String trainerUsername;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private String traineeUsername;
}
