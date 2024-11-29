package org.example.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.responsedto.ResponseDto;
import org.example.entity.TokenEntity;
import org.example.entity.TokenType;
import org.example.entity.UserEntity;
import org.example.exceptions.GymAuthenticationException;
import org.example.services.JwtService;
import org.example.services.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {
    private final JwtService jwtService;
    private final UserService userService;
    private final ObjectMapper objectMapper;

    /**
     * Setting dependencies.
     */
    public JwtAuthFilter(@Lazy AuthenticationManager authenticationManager,
                         JwtService jwtService,
                         UserService userService,
                         ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher("/login", "GET"));
        setAuthenticationManager(authenticationManager);
        this.jwtService = jwtService;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        log.debug("Running authentication for login request");
        String username = request.getHeader("username");
        String password = request.getHeader("password");


        if (username == null || password == null) {
            log.debug("Bad credentials for login.");
            throw new GymAuthenticationException("Bad credentials.");
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        log.debug("Passing the authentication object to authentication manager.");
        return getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        log.debug("Success Authentication");
        SecurityContextHolder.getContext().setAuthentication(authResult);

        UserEntity user = userService.getUserByUsername(authResult.getName())
            .orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        List<TokenEntity> validTokensForUser = jwtService.findNonRevokedTokensByUser(user.getUsername());

        boolean hasValidToken = validTokensForUser.stream()
            .anyMatch(token -> !jwtService.isTokenExpired(token.getToken()));

        if (hasValidToken) {
            log.debug("User is already logged in.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            ResponseDto<String> responseDto = new ResponseDto<>(
                null,
                "User is already logged in. Please use your valid token or log out before logging in again.");
            objectMapper.writeValue(response.getWriter(), responseDto);
            return;
        }

        String token = jwtService.generateToken(authResult);
        jwtService.saveGeneratedToken(new TokenEntity(token, TokenType.ACCESS, false, user));
        ResponseDto<String> responseDto = new ResponseDto<>(token, "Successfully logged in.");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), responseDto);
    }
}
