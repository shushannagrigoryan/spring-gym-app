package org.example.dto.requestdto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TrainerWorkloadRequestDto {
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private LocalDateTime trainingDate;
    private BigDecimal trainingDuration;
    private ActionType actionType;
}