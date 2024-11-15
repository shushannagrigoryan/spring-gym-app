package org.example.dto.requestdto;

import jakarta.validation.constraints.NotNull;
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
public class TrainerUpdateRequestDto {
    @NotNull(message = "First Name is required.")
    private String firstName;
    @NotNull(message = "Last Name is required.")
    private String lastName;
    private Long specialization;
    @NotNull(message = "IsActive is required.")
    private Boolean isActive;

}
