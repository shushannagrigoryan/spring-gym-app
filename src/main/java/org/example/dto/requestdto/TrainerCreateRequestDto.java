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
public class TrainerCreateRequestDto {
    @NotNull(message = "FirstName is required.")
    private String firstName;
    @NotNull(message = "LastName is required.")
    private String lastName;
    @NotNull(message = "Specialization is required.")
    private Long specialization;
}
