package org.example.auth;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.exceptions.GymIllegalArgumentException;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserAuth {
    private final UserRepository userRepository;

    /**
     * Injecting dependencies using constructor.
     */
    public UserAuth(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Authentication for user by username and password.
     *
     * @param username username of the User
     * @param password password of the user
     * @return true if user exists, else false.
     */
    public boolean userAuth(String username, String password) {
        Optional<UserEntity> user = userRepository.getUserByUsername(username);
        if (user.isEmpty()) {
            log.debug("No user with username: {}", username);
            throw new GymIllegalArgumentException(
                    String.format("No user with username: %s", username));
        }

        if (!user.get().getPassword().equals(password)) {
            log.debug("Incorrect password for user with username: {}", username);
            throw new GymIllegalArgumentException(
                    String.format("Incorrect password for user with username: %s", username));
        }

        return true;
    }

}
