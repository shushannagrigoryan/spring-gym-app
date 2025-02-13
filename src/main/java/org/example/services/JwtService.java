package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TokenEntity;
import org.example.repositories.TokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60;
    private final TokenRepository tokenRepository;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    /**
     * Saving the generated jwt token to database.
     */
    @Transactional
    public void saveGeneratedToken(TokenEntity jwtToken) {
        log.debug("Saving jwtToken in database.");
        tokenRepository.save(jwtToken);
        log.debug("Successfully saved the generated jwt token in database.");
    }

    /**
     * Generates a jwt token using the Authentication object.
     */
    public String generateToken(Authentication authentication) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(authentication.getName())
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusMillis(ACCESS_TOKEN_EXPIRATION))
            .claim("authorities", authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList())
            .build();
        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    /**
     * Checks if the token is revoked.
     *
     * @param token jwt token
     * @return true if token is revoked, otherwise false
     */
    public boolean isTokenRevoked(String token) {
        log.debug("Checking if the token is revoked.");
        TokenEntity tokenEntity = tokenRepository.findByToken(token)
            .orElseThrow(() -> new EntityNotFoundException("Entity not found."));
        return tokenEntity.isRevoked();
    }

    /**
     * Revoking the given token.
     *
     * @param token that should be revoked.
     */
    @Transactional
    public void revokeToken(String token) {
        log.debug("Revoking the given token.");
        int revoked = tokenRepository.updateByTokenSetRevoked(true, token);
        if (revoked == 0) {
            throw new EntityNotFoundException("Token not found");
        }
        log.debug("Successfully revoked the given token");
    }

    /**
     * Checks if the token is valid and token's subject is the given username.
     *
     * @param token    jwt token
     * @param username username of the user
     * @return true if the jwt is valid for the given user.
     */
    public boolean isValid(String token, String username) {
        log.debug("Checking if the token is valid.");
        Jwt jwt;
        try {
            jwt = jwtDecoder.decode(token);
            if (!jwt.getSubject().equals(username)) {
                return false;
            }
            return tokenRepository.findByToken(token).filter(entity -> !entity.isRevoked()).isPresent();
        } catch (JwtException e) {
            log.debug(e.getMessage());
            return false;
        }
    }
}
