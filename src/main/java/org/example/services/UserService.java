package org.example.services;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public UserEntity getUserByUsername(String username) {
        log.debug("Getting user with username: {}", username);
        UserEntity user = userRepository.getUserByUsername(username);
        if (user == null) {
            log.debug("No user with username: {}", username);
        }
        return user;
    }

}
