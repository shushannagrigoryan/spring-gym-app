package org.example.dto.requestdto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class ChangePasswordRequestDto {
    @NotNull(message = "Password is required.")
    private String password;
    @NotNull(message = "New Password is required.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*+-=]+$",
        message = "Password must contain characters: a-z, A-Z, 0-9, and !@#$%^&*+-=")
    private String newPassword;
}
