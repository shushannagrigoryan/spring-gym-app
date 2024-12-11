package securitytest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.example.entity.UserEntity;
import org.example.exceptions.GymAuthenticationException;
import org.example.security.CustomLogoutHandler;
import org.example.services.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@ExtendWith(MockitoExtension.class)
public class CustomLogoutHandlerTest {
    @Mock
    private JwtService jwtService;
    @Mock
    private JwtDecoder jwtDecoder;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private CustomLogoutHandler customLogoutHandler;

    @Test
    public void testLogoutFail() {
        String jwt = "token";
        //given
        when(request.getHeader("Authorization")).thenReturn(jwt);


        //then
        assertThrows(GymAuthenticationException.class,
            () -> customLogoutHandler.logout(request, response, authentication), "Authentication failed.");

    }

    @Test
    public void testLogoutRevokedJwt() {
        //given
        String jwt = "Bearer token";
        String username = "user";
        when(request.getHeader("Authorization")).thenReturn(jwt);
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        Jwt jwtToken = new Jwt(jwt.substring(7), Instant.now(), Instant.now().plusMillis(1000),
            headers, claims);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        when(jwtDecoder.decode("token")).thenReturn(jwtToken);
        when(jwtService.isTokenRevoked(jwtToken.getTokenValue())).thenReturn(true);

        //then
        assertThrows(GymAuthenticationException.class,
            () -> customLogoutHandler.logout(request, response, authentication), "Authentication failed.");
        verify(jwtDecoder).decode("token");
        verify(jwtService).isTokenRevoked(jwtToken.getTokenValue());

    }

    @Test
    public void testLogout() {
        //given
        String jwt = "Bearer token";
        String username = "user";
        when(request.getHeader("Authorization")).thenReturn(jwt);
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        Jwt jwtToken = new Jwt(jwt.substring(7), Instant.now(), Instant.now().plusMillis(1000),
            headers, claims);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        when(jwtDecoder.decode("token")).thenReturn(jwtToken);
        when(jwtService.isTokenRevoked(jwtToken.getTokenValue())).thenReturn(false);
        doNothing().when(jwtService).revokeToken(jwtToken.getTokenValue());


        //when
        customLogoutHandler.logout(request, response, authentication);

        //then
        verify(jwtDecoder).decode("token");
        verify(jwtService).isTokenRevoked(jwtToken.getTokenValue());
        verify(jwtService).revokeToken(jwtToken.getTokenValue());
    }
}
