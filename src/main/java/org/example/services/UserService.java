package org.example.services;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.metrics.UserMetrics;
import org.example.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMetrics userMetrics;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initUserGauge() {
        userMetrics.updateGauge(userRepository.countByActive(true));
    }

    /**
     * Saving a new user in the service layer.
     *
     * @param userEntity the new {@code UserEntity}
     */
    @Transactional
    public UserEntity save(UserEntity userEntity) {
        log.debug("Saving user: {}", userEntity);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        UserEntity user = userRepository.save(userEntity);
        userMetrics.incrementCounter();
        log.debug("Successfully saved user: {}", user);
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
    @Transactional
    public Optional<UserEntity> getUserByUsername(String username) {
        log.debug("Getting user with username");
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.debug("No user with username");
        }
        return user;
    }

    /**
     * Login a user.
     *
     * @param username of the user
     * @param password of the user
     */
    @Transactional
    public void login(String username, String password) {
        log.debug("Logging in user with username and password");
        Optional<UserEntity> user = userRepository.findByUsernameAndPassword(username, password);
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
    public void changeUserPassword(String username, String oldPassword, String newPassword) {
        log.debug("Changing the password of user with username");
        String errorMessage = String.format("No user with username: %s and password: %s", username, oldPassword);
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new GymEntityNotFoundException(errorMessage));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new GymEntityNotFoundException(errorMessage);
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.debug("Successfully changed password of user with username");
    }

    /**
     * Deletes a user.
     *
     * @param user {@code UserEntity} to delete
     */
    @Transactional
    public void deleteUser(UserEntity user) {
        log.debug("Deleting user with username");
        userRepository.deleteById(user.getId());
        log.debug("Successfully deleted user with username");
    }

    /**
     * Returns max username index for users with given firstName and lastName.
     *
     * @param firstName fistName of the user
     * @param lastName  lastName of the user
     * @return max index
     */
    @Transactional
    public Long getUsernameMaxIndex(String firstName, String lastName) {
        log.debug("Getting max username index for users with firstName: {} and lastName: {}",
            firstName, lastName);
        return userRepository.findUsernameMaxIndex(firstName, lastName);
    }

}
