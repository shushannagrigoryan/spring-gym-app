package org.example.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.GymUserBlockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.debug("Authentication failure handler.");
        response.setContentType("application/json");
        if (exception.getCause() instanceof GymUserBlockedException) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(String.format("{\"message\": \"%s\"}", exception.getMessage()));
            return;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(String.format("{\"message\": \"%s\"}", exception.getMessage()));

    }
}