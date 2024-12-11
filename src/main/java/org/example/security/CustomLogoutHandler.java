package org.example.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.GymAuthenticationException;
import org.example.services.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomLogoutHandler implements LogoutHandler {
    private final JwtService jwtService;
    private final JwtDecoder jwtDecoder;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.debug("Running logout handler.");
        String authHeader = request.getHeader("Authorization");
        log.debug(authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No token provided.");
            throw new GymAuthenticationException("Authentication failed.");
        }

        String jwtToken = authHeader.substring(7);
        Jwt jwt = jwtDecoder.decode(jwtToken);

        if (jwtService.isTokenRevoked(jwt.getTokenValue())) {
            throw new GymAuthenticationException("Authentication failed.");
        }

        jwtService.revokeToken(jwt.getTokenValue());

        log.debug("Successfully logged out and invalidated users tokens.");
    }
}
