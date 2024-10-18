package org.example.dto.responsedto;

import java.time.LocalDate;
import java.util.Set;
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
public class TraineeProfileResponseDto {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
    private Set<TraineeProfileTrainerResponseDto> trainers;

}
