package org.example.services;

import jakarta.persistence.EntityNotFoundException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TokenEntity;
import org.example.entity.TokenType;
import org.example.entity.UserEntity;
import org.example.exceptions.GymAuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RefreshTokenService {
    private final GymUserDetailService userDetailService;
    private final JwtService jwtService;
    private final UserService userService;

    /**
     * Setting dependencies.
     */
    public RefreshTokenService(GymUserDetailService userDetailService, JwtService jwtService, UserService userService) {
        this.userDetailService = userDetailService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    /**
     * Refreshing the access token from the given refresh token.
     *
     * @param refreshToken the given refresh token
     * @return {@code String} the generated access token
     */
    public String refreshAccessToken(String refreshToken) {
        log.debug("Generating a new access token from the given refresh token.");
        String username = jwtService.getUsernameFromJwt(refreshToken);
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        String tokenType = jwtService.extractClaim(refreshToken, claims -> claims.get("Type")).toString();

        boolean isTokenRevoked = jwtService.isTokenRevoked(refreshToken);

        if (jwtService.isTokenValid(refreshToken, userDetails)
            && tokenType.equals(TokenType.REFRESH.name()) && isTokenRevoked) {
            log.debug("Invalid refresh token.");
            throw new GymAuthenticationException("Invalid refresh token.");
        }

        String newAccessToken = jwtService.generateAccessToken(Map.of("Type", TokenType.ACCESS), userDetails);
        UserEntity user = userService.getUserByUsername(username).orElseThrow(
            () -> new EntityNotFoundException("No user found."));

        jwtService.saveGeneratedToken(new TokenEntity(newAccessToken, TokenType.ACCESS,
            false, user));
        log.debug("New access token: {}", newAccessToken);
        return newAccessToken;
    }
}
