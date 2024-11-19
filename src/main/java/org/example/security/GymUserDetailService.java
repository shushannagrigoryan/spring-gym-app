package org.example.security;

import org.example.entity.UserEntity;
import org.example.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GymUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Setting dependencies.
     */
    public GymUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found: %s", username)));
        return User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            //.roles(myUser.getRoles().split(","))
            .build();
    }
}
