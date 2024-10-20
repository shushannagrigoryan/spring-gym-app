package org.example.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptionhandlers.ExceptionResponse;
import org.example.exceptions.GymAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    private final UserAuth userAuth;
    private final ObjectMapper objectMapper;

    /** Setting dependencies. */
    public AuthInterceptor(UserAuth userAuth, ObjectMapper objectMapper) {
        this.userAuth = userAuth;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response,
                              @NonNull Object handler) throws Exception {
        String username = request.getHeader("username");
        String password = request.getHeader("password");

        log.debug("Performing authentication.");

        try {
            userAuth.userAuth(username, password);
        } catch (GymAuthenticationException e) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
            String json = objectWriter.writeValueAsString(
                    new ExceptionResponse("Authentication failed: " + e.getMessage(),
                            HttpStatus.UNAUTHORIZED));
            response.getWriter().write(json);
            return false;
        }

        log.debug("Authenticating succeeded.");
        return true;
    }
}
