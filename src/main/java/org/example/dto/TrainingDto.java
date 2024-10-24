package org.example.dto;

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
public class TrainingDto {
    private Long traineeId;
    private Long trainerId;
    private String trainingName;
    private Long trainingType;
    private LocalDateTime trainingDate;
    private BigDecimal trainingDuration;


}
