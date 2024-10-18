package org.example.dto.responsedto;

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
public class TraineeProfileTrainerResponseDto {
    private String username;
    private String firstName;
    private String lastName;
    private TrainingTypeResponseDto specialization;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return username.equals(((TraineeProfileTrainerResponseDto) o).username);
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}
