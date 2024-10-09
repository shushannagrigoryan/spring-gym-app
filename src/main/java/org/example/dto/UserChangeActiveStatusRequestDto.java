package org.example.dto;

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
    @NotNull(message = "Username is required.")
    private String username;
    @NotNull(message = "IsActive is required.")
    private Boolean isActive;
}
