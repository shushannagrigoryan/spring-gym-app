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
public class TrainerProfileTraineeResponseDto {
    private String username;
    private String firstName;
    private String lastName;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return username.equals(((TrainerProfileTraineeResponseDto) o).username);
    }

    @Override
    public int hashCode() {
        return username != null ? username.hashCode() : 0;
    }
}
