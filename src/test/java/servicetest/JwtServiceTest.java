package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import org.example.entity.TokenEntity;
import org.example.entity.TokenType;
import org.example.entity.UserEntity;
import org.example.repositories.TokenRepository;
import org.example.services.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private Authentication authentication;
    @Mock
    private JwtEncoder jwtEncoder;
    @Mock
    private JwtDecoder jwtDecoder;
    @Mock
    private Jwt jwt;
    @InjectMocks
    private JwtService jwtService;


    @Test
    public void testSaveGeneratedToken() {
        //given
        UserEntity user = new UserEntity();
        TokenEntity jwtToken = new TokenEntity("token", TokenType.ACCESS, false, user);
        when(tokenRepository.save(jwtToken)).thenReturn(jwtToken);

        //when
        jwtService.saveGeneratedToken(jwtToken);

        //then
        verify(tokenRepository).save(jwtToken);
    }

    @Test
    public void testIsTokenRevokedSuccess() {
        //given
        String token = "token";
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setRevoked(true);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));

        //when
        boolean result = jwtService.isTokenRevoked(token);

        //then
        assertTrue(result);
        verify(tokenRepository).findByToken(token);

    }

    @Test
    public void testIsTokenRevokedTokenNotFound() {
        //given
        String token = "token";
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setRevoked(true);
        when(tokenRepository.findByToken(token)).thenThrow(new EntityNotFoundException("Entity not found"));

        //then
        assertThrows(EntityNotFoundException.class, () -> jwtService.isTokenRevoked(token),
            "Entity not found");

        verify(tokenRepository).findByToken(token);

    }

    @Test
    public void testGenerateToken() {
        //given
        String expectedToken = "test-token";
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        when(authentication.getAuthorities()).thenAnswer(x -> authorities);
        Jwt jwt = mock(Jwt.class);
        when(jwt.getTokenValue()).thenReturn(expectedToken);
        when(authentication.getName()).thenReturn("test-user");
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwt);

        //when
        String actualToken = jwtService.generateToken(authentication);

        //then
        assertEquals(expectedToken, actualToken);
        verify(authentication).getName();
        verify(authentication).getAuthorities();
    }

    @Test
    public void testRevokeTokenSuccess() {
        //given
        String token = "token";
        when(tokenRepository.updateByTokenSetRevoked(true, token)).thenReturn(1);

        //when
        jwtService.revokeToken(token);

        //then
        verify(tokenRepository).updateByTokenSetRevoked(true, token);
    }

    @Test
    public void testRevokeTokenFailure() {
        //given
        String token = "token";
        when(tokenRepository.updateByTokenSetRevoked(true, token)).thenReturn(0);

        //then
        assertThrows(EntityNotFoundException.class, () -> jwtService.revokeToken(token),
            "Token not found");
    }

    @Test
    public void testIsValid() {
        //given
        String username = "user";
        String token = "token";
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setRevoked(false);
        when(jwtDecoder.decode(token)).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn(username);
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(tokenEntity));

        //when
        boolean result = jwtService.isValid(token, username);

        //then
        verify(jwtDecoder).decode(token);
        verify(jwt).getSubject();
        verify(tokenRepository).findByToken(token);
        assertTrue(result);
    }

    @Test
    public void testIsValid_Not_Valid() {
        //given
        String username = "user";
        String token = "token";
        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setToken(token);
        tokenEntity.setRevoked(false);
        when(jwtDecoder.decode(token)).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn("user1");

        //when
        boolean result = jwtService.isValid(token, username);

        //then
        verify(jwtDecoder).decode(token);
        verify(jwt).getSubject();
        assertFalse(result);
    }

    @Test
    public void testIsValid_Not_Valid_Decode_Fail() {
        //given
        String username = "user";
        String token = "token";
        when(jwtDecoder.decode(token)).thenThrow(JwtException.class);

        //when
        boolean result = jwtService.isValid(token, username);

        //then
        verify(jwtDecoder).decode(token);
        assertFalse(result);
    }
}
