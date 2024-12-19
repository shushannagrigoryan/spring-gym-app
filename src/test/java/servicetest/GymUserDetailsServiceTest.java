package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;
import org.example.entity.Role;
import org.example.entity.UserEntity;
import org.example.services.GymUserDetailsService;
import org.example.services.LoginAttemptService;
import org.example.services.UserService;
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
    private UserService userService;
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
        user.setRoles(Set.of(Role.TRAINEE));
        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));

        //when
        UserDetails result = userDetailsService.loadUserByUsername(username);

        //then
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals("password", result.getPassword());
        assertTrue(result.isEnabled());
        verify(userService).getUserByUsername(username);
    }

    @Test
    public void testLoadUserByUsernameUserNotFound() {
        //given
        String username = "user";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword("password");
        user.setRoles(Set.of(Role.TRAINEE));
        when(userService.getUserByUsername(username)).thenReturn(Optional.empty());

        //then
        assertThrows(UsernameNotFoundException.class, () ->
            userDetailsService.loadUserByUsername(username), String.format("User not found: %s", username));
        verify(userService).getUserByUsername(username);
        verifyNoInteractions(loginAttemptService);
    }

}
