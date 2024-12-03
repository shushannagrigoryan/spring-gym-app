package org.example.services;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.LoginAttemptEntity;
import org.example.entity.UserEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.repositories.LoginAttemptRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LoginAttemptService {
    private static final int MAX_FAIL_ATTEMPT = 3;
    private static final int BLOCK_TIME = 5;
    private final LoginAttemptRepository loginAttemptRepository;

    /**
     * Incrementing the number of failed login attempts for the given username.
     */
    @Transactional
    public void loginFailed(UserEntity user) {
        log.debug("Failed login for user: {}", user.getUsername());
        LoginAttemptEntity loginAttempt =
            loginAttemptRepository.findByUser_Username(user.getUsername()).orElse(new LoginAttemptEntity());

        LocalDateTime lastFailedAttempt = loginAttempt.getLastFailedAttempt();

        if (lastFailedAttempt != null && lastFailedAttempt.isBefore(LocalDateTime.now().minusMinutes(BLOCK_TIME))) {
            //Clearing the login attempt failure entity for the given user is the block time has exceeded.
            log.debug("Clearing attempt failure entity for user: {}", user);
            clearFailedLogin(user.getUsername());
        }

        loginAttempt.setUser(user);
        loginAttempt.setFailedCount(loginAttempt.getFailedCount() + 1);
        loginAttempt.setLastFailedAttempt(LocalDateTime.now());
        loginAttemptRepository.save(loginAttempt);
    }

    /**
     * Checks if the number of failed login attempts exceeds the given threshold.
     */
    public boolean isBlocked(UserEntity user) {
        log.debug("Checking if the user is blocked.");
        Optional<LoginAttemptEntity> loginAttempt =
            loginAttemptRepository.findByUser_Username(user.getUsername());

        if (loginAttempt.isPresent()) {
            if (loginAttempt.get().getFailedCount() >= MAX_FAIL_ATTEMPT
                && loginAttempt.get().getLastFailedAttempt().isAfter(LocalDateTime.now().minusMinutes(BLOCK_TIME))) {
                log.debug("Too many failed login attempts.");
                return true;
            }
        }
        return false;

    }

    /**
     * Clearing failed login attempt(fail count and last fail time) entity for the given user.
     *
     * @param username username of the user.
     */
    @Transactional
    public void clearFailedLogin(String username) {
        log.debug("Clearing failed attempt entity data for user {} ", username);
        LoginAttemptEntity loginAttempt =
            loginAttemptRepository.findByUser_Username(username)
                .orElseThrow(() -> new GymEntityNotFoundException("FailedAttemptEntity not found."));
        loginAttempt.setFailedCount(0);
        loginAttempt.setLastFailedAttempt(null);
        loginAttemptRepository.save(loginAttempt);
    }
}