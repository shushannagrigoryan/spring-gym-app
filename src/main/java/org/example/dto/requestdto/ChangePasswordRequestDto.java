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
public class ChangePasswordRequestDto {
    @NotNull(message = "Username is required.")
    private String username;
    @NotNull(message = "Password is required.")
    private String password;
    @NotNull(message = "New Password is required.")
    private String newPassword;
}
