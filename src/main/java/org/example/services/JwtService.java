package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TokenEntity;
import org.example.entity.UserEntity;
import org.example.repositories.TokenRepository;
import org.example.security.JwtCustomEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 60;
    private final TokenRepository tokenRepository;
    private final JwtCustomEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    //private final JwtCustomDecoder jwtDecoder;

    /**
     * Saving the generated jwt token to database.
     */
    public void saveGeneratedToken(TokenEntity jwtToken) {
        log.debug("Saving jwtToken in database.");
        tokenRepository.save(jwtToken);
        log.debug("Successfully saved the generated jwt token in database.");
    }


    public List<TokenEntity> findNonRevokedTokensByUser(String username) {
        log.debug("Getting non revoked tokens of user {}.", username);
        return tokenRepository.findByUser_UsernameAndRevoked(username, false);
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
        JwtEncoder encoder = jwtEncoder.jwtEncoder();
        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    /**
     * Checks if the token is expired.
     *
     * @param token jwt token
     * @return true if token is invalid, otherwise false.
     */
    public boolean isTokenExpired(String token) {
        log.debug("Checking if the token is expired.");
        try {
            jwtDecoder.decode(token);
            log.debug("Token is not expired.");
            return false;
        } catch (JwtValidationException e) {
            log.debug(e.getMessage());
        }
        log.debug("Token is expired.");
        return true;
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
     * Revoking user's active tokens.
     *
     * @param user user for whose tokens should be revoked.
     */
    public void revokeAllUserTokens(UserEntity user) {
        log.debug("Revoking user's all active tokens.");
        List<TokenEntity> activeTokens = tokenRepository
            .findByUserAndRevoked(user, false);
        if (activeTokens.isEmpty()) {
            return;
        }

        activeTokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(activeTokens);
    }
}
