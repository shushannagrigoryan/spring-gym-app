package org.example.dto.requestdto;

import jakarta.validation.constraints.NotBlank;
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
public class UserChangeActiveStatusRequestDto {
    @NotBlank(message = "Username is required and can't be blank.")
    private String username;
    @NotNull(message = "IsActive is required.")
    private Boolean isActive;
}
