package org.example.services;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    private final UserRepo userRepository;

    public UserService(UserRepo userRepository) {
        this.userRepository = userRepository;
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
        return userRepository.getAllUsernames();
    }

    /**
     * Getting user by username.
     *
     * @param username username of the user
     * @return {@code UserEntity} if user exists, else null.
     */
    @Transactional
    public Optional<UserEntity> getUserByUsername(String username) {
        log.debug("Getting user with username: {}", username);
        Optional<UserEntity> user = userRepository.getUserByUsername(username);
        if (user.isEmpty()) {
            log.debug("No user with username: {}", username);
        }
        return user;
    }

    /**
     * Login a user.
     *
     * @param username of the user
     * @param password of the user
     */
    public void login(String username, String password) {
        log.debug("Logging in user with username {} and password {}", username, password);
        Optional<UserEntity> user = userRepository.getUserByUsernameAndPassword(username, password);
        if (user.isEmpty()) {
            throw new GymEntityNotFoundException("Invalid username and password.");
        }
        log.debug("Successfully logged in.");
    }

    /**
     * Changes password of the user.
     *
     * @param username    username of the user
     * @param newPassword new password of the user.
     */
    @Transactional
    public void changeUserPassword(String username, String newPassword) {
        log.debug("Changing the password of user with username: {}", username);
        userRepository.updatePassword(username, newPassword);
        log.debug("Successfully changed password of user with username: {}", username);
    }

    /**
     * Deletes a user.
     *
     * @param user {@code UserEntity} to delete
     */
    @Transactional
    public void deleteUser(UserEntity user) {
        log.debug("Deleting user with username: {}", user.getUsername());
        userRepository.deleteById(user.getId());
        log.debug("Successfully deleted user with username: {}", user.getUsername());
    }

}
