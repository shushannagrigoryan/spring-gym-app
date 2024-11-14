package org.example.dto.requestdto;

import jakarta.validation.constraints.NotNull;
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
public class TraineeUpdateRequestDto {
    @NotNull(message = "First name is required")
    private String firstName;
    @NotNull(message = "Last name is required")
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    @NotNull(message = "IsActive is required")
    private Boolean isActive;
}
