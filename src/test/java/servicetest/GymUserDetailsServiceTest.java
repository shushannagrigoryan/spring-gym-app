package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.entity.Role;
import org.example.entity.UserEntity;
import org.example.repositories.UserRepository;
import org.example.services.GymUserDetailsService;
import org.example.services.LoginAttemptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
public class GymUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private LoginAttemptService loginAttemptService;
    @InjectMocks
    private GymUserDetailsService userDetailsService;

    @Test
    public void testLoadUserByUsernameSuccess() {
        //given
        String username = "user";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole(Role.TRAINEE);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(loginAttemptService.isBlocked(user)).thenReturn(false);

        //when
        UserDetails result = userDetailsService.loadUserByUsername(username);

        //then
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("password", result.getPassword());
        assertTrue(result.isEnabled());
        verify(userRepository).findByUsername(username);
        verify(loginAttemptService).isBlocked(user);
    }

    @Test
    public void testLoadUserByUsernameUserNotFound() {
        //given
        String username = "user";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole(Role.TRAINEE);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        //then
        assertThrows(UsernameNotFoundException.class, () ->
            userDetailsService.loadUserByUsername(username), String.format("User not found: %s", username));
        verify(userRepository).findByUsername(username);
        verifyNoInteractions(loginAttemptService);
    }

    @Test
    public void testLoadUserByUsernameUserBlocked() {
        //given
        String username = "user";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole(Role.TRAINEE);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(loginAttemptService.isBlocked(user)).thenReturn(true);

        //when
        UserDetails result = userDetailsService.loadUserByUsername(username);

        //then
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("password", result.getPassword());
        assertFalse(result.isEnabled());
        verify(userRepository).findByUsername(username);
        verify(loginAttemptService).isBlocked(user);
    }
}
