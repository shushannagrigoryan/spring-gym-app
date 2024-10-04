package org.example.services;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
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

    public Optional<UserEntity> getUserByUsername(String username) {
        log.debug("Getting user with username: {}", username);
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            log.debug("No user with username: {}", username);
        }
        return user;
    }

    //    public UserEntity changeUserPassword(String username, String newPassword) {
    //        log.debug("Updating the password of trainee with username: {}", username);
    //        return userRepository.updatePassword(username, newPassword);
    //    }



}
