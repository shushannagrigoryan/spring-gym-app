package securitytest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.GymAuthenticationException;
import org.example.security.CustomAuthenticationFailureHandler;
import org.example.security.CustomAuthenticationSuccessHandler;
import org.example.security.CustomUsernamePasswordAuthenticationFilter;
import org.example.services.LoginAttemptService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class CustomUsernamePasswordAuthenticationFilterTest {
    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;
    @Mock
    private CustomAuthenticationSuccessHandler successHandler;
    @Mock
    private CustomAuthenticationFailureHandler failureHandler;
    @Mock
    private LoginAttemptService loginAttemptService;
    @InjectMocks
    private CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter;

    @Test
    void attemptAuthenticationSuccess() {
        //given

        when(request.getRemoteAddr()).thenReturn(null);
        when(request.getHeader("username")).thenReturn("user");
        when(request.getHeader("password")).thenReturn("pass");

        UsernamePasswordAuthenticationToken authRequest =
            new UsernamePasswordAuthenticationToken("user", "pass");
        when(authenticationManager.authenticate(authRequest)).thenReturn(authentication);

        //when
        customUsernamePasswordAuthenticationFilter.attemptAuthentication(request, response);

        //then
        verify(authenticationManager).authenticate(authRequest);
        assertNotNull(successHandler);
        assertNotNull(failureHandler);
    }

    @Test
    void attemptAuthenticationUserBlocked() {
        //given
        String ipAddress = "ipAddress";
        when(request.getRemoteAddr()).thenReturn(ipAddress);
        when(loginAttemptService.isBlocked(ipAddress)).thenReturn(true);

        //then
        assertThrows(GymAuthenticationException.class, () ->
                customUsernamePasswordAuthenticationFilter.attemptAuthentication(request, response),
            ("Too many failed login attempts, try again later."));
    }

    @Test
    void attemptAuthenticationFail() {
        //given
        when(request.getHeader("username")).thenReturn(null);
        when(request.getHeader("password")).thenReturn(null);

        //then
        assertThrows(GymAuthenticationException.class,
            () -> customUsernamePasswordAuthenticationFilter.attemptAuthentication(request, response),
            "Bad credentials.");

    }

}
