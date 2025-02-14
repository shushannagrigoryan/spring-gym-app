package org.example.services;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.LoginAttemptEntity;
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
    public void loginFailed(String userIp) {
        log.debug("Failed login for user with ip address");
        LoginAttemptEntity loginAttempt = loginAttemptRepository.findByUserIp(userIp).orElse(new LoginAttemptEntity());

        LocalDateTime lastFailedAttempt = loginAttempt.getLastFailedAttempt();

        if (lastFailedAttempt != null && lastFailedAttempt.isBefore(LocalDateTime.now().minusMinutes(BLOCK_TIME))) {
            //Clearing the login attempt failure entity for the given user if the block time has exceeded.
            log.debug("Clearing attempt failure entity for user with given ip");
            loginAttempt = clearFailedLogin(userIp);
        }
        if (loginAttempt.getFailedCount() >= MAX_FAIL_ATTEMPT) {
            log.debug("Number of login fails exceeds {}", MAX_FAIL_ATTEMPT);
            return;
        }
        loginAttempt.setUserIp(userIp);
        loginAttempt.setFailedCount(loginAttempt.getFailedCount() + 1);
        loginAttempt.setLastFailedAttempt(LocalDateTime.now());
        loginAttemptRepository.save(loginAttempt);
    }

    /**
     * Checks if the number of failed login attempts exceeds the given threshold.
     */
    public boolean isBlocked(String userIp) {
        log.debug("Checking if the user is blocked.");
        Optional<LoginAttemptEntity> loginAttempt = loginAttemptRepository.findByUserIp(userIp);

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
     * @param userIp ip address of the user.
     */
    @Transactional
    public LoginAttemptEntity clearFailedLogin(String userIp) {
        log.debug("Clearing failed attempt entity data for user");
        LoginAttemptEntity loginAttempt =
            loginAttemptRepository.findByUserIp(userIp)
                .orElseThrow(() -> new GymEntityNotFoundException("FailedAttemptEntity not found."));
        loginAttempt.setFailedCount(0);
        loginAttempt.setLastFailedAttempt(null);
        loginAttemptRepository.save(loginAttempt);
        return loginAttempt;
    }
}