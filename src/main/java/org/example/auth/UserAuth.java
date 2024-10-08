package org.example.auth;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.exceptions.GymAuthenticationException;
import org.example.services.UserService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserAuth {
    private final UserService userService;

    /**
     * Setting dependencies.
     */
    public UserAuth(UserService userService) {
        this.userService = userService;
    }

    /**
     * Authentication for user by username and password.
     * Throws {@code GymAuthenticationException} if authentication fails.
     *
     * @param username username of the user
     * @param password password of the user
     */
    public void userAuth(String username, String password) {
        log.debug("Authentication for user with username: {} and password: {}", username, password);
        if (username == null || password == null) {
            log.debug("Authentication username or password missing.");
            throw new GymAuthenticationException("Please enter username and password.");
        }
        Optional<UserEntity> user = userService.getUserByUsername(username);
        if (user.isEmpty()) {
            log.debug("No user with username: {}", username);
            throw new GymAuthenticationException(
                    String.format("No user with username: %s", username));
        }

        if (!user.get().getPassword().equals(password)) {
            log.debug("Incorrect password for user with username: {}", username);
            throw new GymAuthenticationException(
                    String.format("Incorrect password for user with username: %s",
                            username));
        }

    }

}
