package org.example.services;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.GymAuthenticationException;
import org.example.security.GymUserDetailService;
import org.example.security.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RefreshTokenService {
    private static final String TOKEN_TYPE_REFRESH = "Refresh Token";
    private static final String TOKEN_TYPE_ACCESS = "Access Token";
    private final GymUserDetailService userDetailService;
    private final JwtService jwtService;

    public RefreshTokenService(GymUserDetailService userDetailService, JwtService jwtService) {
        this.userDetailService = userDetailService;
        this.jwtService = jwtService;
    }

    /**
     * Refreshing the access token from the given refresh token.
     *
     * @param refreshToken the given refresh token
     * @return {@code String} the generated access token
     */
    public String refreshAccessToken(String refreshToken) {
        log.debug("Generating a new access token from the given access token.");
        String username = jwtService.getUsernameFromJwt(refreshToken);
        UserDetails userDetails = userDetailService.loadUserByUsername(username);
        String tokenType = jwtService.extractClaim(refreshToken, claims -> claims.get("Type")).toString();
        if (refreshToken == null || !jwtService.isTokenValid(refreshToken, userDetails)
            || !tokenType.equals(TOKEN_TYPE_REFRESH)) {
            log.debug("Invalid refresh token.");
            throw new GymAuthenticationException("Invalid refresh token.");
        }

        String newAccessToken = jwtService.generateAccessToken(Map.of("Type", TOKEN_TYPE_ACCESS), userDetails);
        log.debug("New access token: {}", newAccessToken);
        return newAccessToken;
    }
}
