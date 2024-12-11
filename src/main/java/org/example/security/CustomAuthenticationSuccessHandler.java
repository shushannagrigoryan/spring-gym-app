package org.example.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.responsedto.ResponseDto;
import org.example.entity.TokenEntity;
import org.example.entity.TokenType;
import org.example.entity.UserEntity;
import org.example.services.JwtService;
import org.example.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final UserService userService;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.debug("Success Authentication");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserEntity user = userService.getUserByUsername(authentication.getName())
            .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        log.debug("Checking if the user is already logged in.");
        String jwtToken = request.getHeader("Authorization");


        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            if (jwtService.isValid(jwtToken.substring(7), user.getUsername())) {
                log.debug("User is already logged in.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                ResponseDto<String> responseDto = new ResponseDto<>(
                    null,
                    "User is already logged in. Please use your valid token or log out before logging in again.");
                objectMapper.writeValue(response.getWriter(), responseDto);
                return;
            }
        }

        String token = jwtService.generateToken(authentication);
        jwtService.saveGeneratedToken(new TokenEntity(token, TokenType.ACCESS, false, user));
        ResponseDto<String> responseDto = new ResponseDto<>(token, "Successfully logged in.");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), responseDto);
    }
}
