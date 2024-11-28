package org.example.services;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.exceptions.GymUserBlockedException;
import org.example.repositories.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@Slf4j
public class GymUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    /**
     * Setting dependencies.
     */
    public GymUserDetailService(UserRepository userRepository,
                                LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
    }


    /**
     * Loads user by username.
     *
     * @param username the username identifying the user whose data is required.
     * @return {@code UserDetails}
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by Username.");
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found: %s", username)));

        log.debug("Building the UserBuilder object.");
        User.UserBuilder userBuilder = User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(String.valueOf(user.getRole()));

        if (loginAttemptService.isBlocked(username)) {
            log.debug("User with username {} is blocked for 5 minutes", username);
            userBuilder.disabled(true);
            throw new GymUserBlockedException("Too many failed attempts. Try again later.");
        }
        log.debug("Successfully built the UserBuilder object.");
        return userBuilder.build();
    }
}
