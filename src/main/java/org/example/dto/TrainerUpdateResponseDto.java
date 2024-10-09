package org.example.dto;

import java.util.List;
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
public class TrainerUpdateResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingTypeResponseDto specialization;
    private Boolean isActive;
    private List<TrainerProfileTraineeResponseDto> trainees;
}
