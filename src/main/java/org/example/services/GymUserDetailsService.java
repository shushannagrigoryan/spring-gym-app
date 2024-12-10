package org.example.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.repositories.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class GymUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;

    /**
     * Loads user by username.
     *
     * @param username the username identifying the user whose data is required.
     * @return {@code UserDetails}
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user by Username.");
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found: %s", username)));
        log.debug("Building the UserBuilder object.");
        User.UserBuilder userBuilder = User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRoles().stream().map(Enum::name).toArray(String[]::new));

        if (loginAttemptService.isBlocked(user)) {
            log.debug("User with username {} is blocked for 5 minutes", username);
            userBuilder.disabled(true);
        }
        log.debug("Successfully built the UserBuilder object.");
        return userBuilder.build();
    }
}
