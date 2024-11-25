package org.example.security;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.UserEntity;
import org.example.services.JwtService;
import org.example.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {
    private final JwtService jwtService;
    private final UserService userService;

    public CustomLogoutHandler(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.debug("Running logout handler.");
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No token provided.");
            return;
        }

        String jwtToken = authHeader.substring(7);
        String username = jwtService.getUsernameFromJwt(jwtToken);
        UserEntity user = userService.getUserByUsername(username).orElseThrow(
            () -> new EntityNotFoundException("User not found.")
        );

        jwtService.revokeAllUserTokens(user);
        log.debug("Successfully logged out and invalidated users tokens.");
    }
}
