package org.example.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.responsedto.ResponseDto;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.debug("Authentication entry point.");
        log.debug("Authentication failed: {}", authException.getMessage());
        ResponseDto<String> responseDto;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        Throwable authExceptionCause = authException.getCause();
        if (authExceptionCause instanceof JwtValidationException) {
            log.debug("JwtValidationException: Invalid jwt was sent.");
            responseDto = new ResponseDto<>(null,
                "Authentication failed.");
            objectMapper.writeValue(response.getWriter(), responseDto);
            return;
        }
        responseDto = new ResponseDto<>(null,
            "Authentication failed: " + authException.getMessage());
        objectMapper.writeValue(response.getWriter(), responseDto);

    }
}
