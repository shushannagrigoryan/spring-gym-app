package securitytest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.example.entity.UserEntity;
import org.example.exceptions.GymEntityNotFoundException;
import org.example.security.AuthenticationFailureListener;
import org.example.services.LoginAttemptService;
import org.example.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;

@ExtendWith(MockitoExtension.class)
public class AuthenticationFailureListenerTest {
    @Mock
    private LoginAttemptService loginAttemptService;
    @Mock
    private UserService userService;
    @InjectMocks
    private AuthenticationFailureListener authenticationFailureListener;

    @Test
    public void testOnApplicationEvent() {
        //given
        String username = "username";
        String password = "password";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);

        AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(
            authenticationToken, new BadCredentialsException("Auth failed."));

        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));

        //when
        authenticationFailureListener.onApplicationEvent(event);

        //then
        verify(loginAttemptService).loginFailed(user);
    }

    @Test
    public void testOnApplicationEventUserNotFound() {
        //given
        String username = "username";
        String password = "password";
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);

        AuthenticationFailureBadCredentialsEvent event = new AuthenticationFailureBadCredentialsEvent(
            authenticationToken, new BadCredentialsException("Auth failed."));

        when(userService.getUserByUsername(username)).thenThrow(GymEntityNotFoundException.class);


        //then
        assertThrows(GymEntityNotFoundException.class, () -> authenticationFailureListener.onApplicationEvent(event),
            "User is not found.");
    }
}
