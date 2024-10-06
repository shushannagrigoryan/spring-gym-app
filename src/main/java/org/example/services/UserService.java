package org.example.services;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.password.PasswordGeneration;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordGeneration passwordGeneration;

    public UserService(UserRepository userRepository,
                       PasswordGeneration passwordGeneration) {
        this.userRepository = userRepository;
        this.passwordGeneration = passwordGeneration;
    }

    /**
     * Creates a new user in the service layer.
     *
     * @param userEntity the new {@code UserEntity}
     */
    @Transactional
    public UserEntity registerUser(UserEntity userEntity) {
        log.debug("Creating user: {}", userEntity);

        UserEntity user = userRepository.save(userEntity);
        log.debug("Successfully created user: {}", user);
        return user;
    }

    /**
     * Get all usernames(both trainer and trainee).
     */
    @Transactional
    public List<String> getAllUsernames() {
        log.debug("Getting all usernames.");
        return userRepository.findAllUsernames();
    }

    /**
     * Getting user by username.
     *
     * @param username username of the user
     * @return {@code UserEntity} if user exists, else null.
     */

    public Optional<UserEntity> getUserByUsername(String username) {
        log.debug("Getting user with username: {}", username);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.debug("No user with username: {}", username);
        }
        return user;
    }

    /** login. */
    public void login(String username, String password) {
        log.debug("Logging in user with username {} and password {}", username, password);
        Optional<UserEntity> user = userRepository.findByUsernameAndPassword(username, password);
        if (user.isEmpty()) {
            throw new GymEntityNotFoundException("Invalid username and password.");
        }
        log.debug("Successfully logged in.");
    }

    /** change password. */
    @Transactional
    public int changeUserPassword(String username) {
        log.debug("Updating the password of trainee with username: {}", username);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        log.debug("Generating new password.");
        int result = userRepository.updatePassword(username, passwordGeneration.generatePassword()
        );
        log.debug("Successfully updated username for user with username: {}", username);
        //return result;
        return result;
    }

}
