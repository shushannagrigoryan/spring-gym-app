package securitytest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import org.example.security.JwtAuthConverter;
import org.example.services.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@ExtendWith(MockitoExtension.class)
public class JwtAuthConverterTest {
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private JwtAuthConverter jwtAuthConverter;

    @Test
    void convertValidToken() {
        //given
        String validToken = "token";
        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn(validToken);
        when(jwt.getClaim("authorities")).thenReturn(List.of("ROLE_TRAINEE"));
        when(jwtService.isTokenRevoked(validToken)).thenReturn(false);

        //when
        JwtAuthenticationToken token = jwtAuthConverter.convert(jwt);

        //then
        assertNotNull(token);
        Collection<?> authorities = token.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.toString().equals("ROLE_TRAINEE")));

        verify(jwtService).isTokenRevoked(validToken);
        verify(jwt).getClaim("authorities");
    }

    @Test
    void convertRevokedToken() {
        //given
        String revokedToken = "token";
        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn(revokedToken);
        when(jwtService.isTokenRevoked(revokedToken)).thenReturn(true);

        //then
        assertThrows(
            JwtException.class, () -> jwtAuthConverter.convert(jwt), "Token is revoked");
        verify(jwtService).isTokenRevoked(revokedToken);
    }
}
