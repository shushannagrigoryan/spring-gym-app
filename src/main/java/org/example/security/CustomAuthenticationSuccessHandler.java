package org.example.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final String TOKEN_TYPE_REFRESH = "Refresh Token";
    private static final String TOKEN_TYPE_ACCESS = "Access Token";
    private final JwtService jwtService;
    private final GymUserDetailService userDetailsService;

    /**
     * Setting dependencies.
     */
    public CustomAuthenticationSuccessHandler(JwtService jwtService,
                                              GymUserDetailService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.debug("Authentication success handler.");
        String username = authentication.getName();
        UserDetails user = userDetailsService.loadUserByUsername(username);

        String jwtAccessToken = jwtService.generateAccessToken(Map.of("Type", TOKEN_TYPE_ACCESS), user);
        String jwtRefreshToken = jwtService.generateRefreshToken(Map.of("Type", TOKEN_TYPE_REFRESH), user);


        response.setContentType("application/json");
        response.getWriter().write("{\"AccessToken\": \"" + jwtAccessToken + "\"}");
        response.getWriter().write("{\"RefreshToken\": \"" + jwtRefreshToken + "\"}");
    }
}