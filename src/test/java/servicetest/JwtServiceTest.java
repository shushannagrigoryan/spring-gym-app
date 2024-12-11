package servicetest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtValidationException;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private JwtDecoder jwtDecoder;
    @Mock
    private Authentication authentication;
    @Mock
    private JwtEncoder jwtEncoder;
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
    public void testFindNonRevokedTokensByUser() {
        //given
        String username = "user";
        TokenEntity token = new TokenEntity();
        when(tokenRepository.findByUser_UsernameAndRevoked(username, false)).thenReturn(List.of(token));

        //when
        List<TokenEntity> result = jwtService.findNonRevokedTokensByUser(username);

        //then
        assertNotNull(result);
        assertEquals(List.of(token), result);
        verify(tokenRepository).findByUser_UsernameAndRevoked(username, false);
    }

    @Test
    public void testIsTokenExpiredNonExpired() {
        //given
        String token = "token";
        when(jwtDecoder.decode(token)).thenReturn(mock(Jwt.class));

        //when
        boolean result = jwtService.isTokenExpired(token);

        //then
        assertFalse(result);
    }

    @Test
    public void testIsTokenExpiredExpired() {
        //given
        String token = "token";
        List<OAuth2Error> errors = List.of(new OAuth2Error("error"));
        when(jwtDecoder.decode(token)).thenThrow(new JwtValidationException("Token expired", errors));

        //when
        boolean result = jwtService.isTokenExpired(token);

        //then
        assertTrue(result);
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
    public void testRevokeAllTokens() {
        //given
        UserEntity user = new UserEntity();
        TokenEntity token = new TokenEntity();
        List<TokenEntity> tokenEntityList = List.of(token);
        when(tokenRepository.findByUserAndRevoked(user, false)).thenReturn(tokenEntityList);
        when(tokenRepository.saveAll(tokenEntityList)).thenReturn(tokenEntityList);

        //when
        jwtService.revokeAllUserTokens(user);

        //then
        verify(tokenRepository).findByUserAndRevoked(user, false);
        verify(tokenRepository).saveAll(tokenEntityList);
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
}
