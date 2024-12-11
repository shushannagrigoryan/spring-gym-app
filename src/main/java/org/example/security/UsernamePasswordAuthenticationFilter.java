package org.example.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.GymAuthenticationException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final RequestMatcher LOGIN_REQUEST_MATCHER = new AntPathRequestMatcher("/login", "GET");

    /**
     * Setting dependencies.
     */
    public UsernamePasswordAuthenticationFilter(@Lazy AuthenticationManager authenticationManager,
                                                CustomAuthenticationSuccessHandler successHandler,
                                                CustomAuthenticationFailureHandler failureHandler) {
        super(LOGIN_REQUEST_MATCHER);
        setAuthenticationManager(authenticationManager);
        setAuthenticationSuccessHandler(successHandler);
        setAuthenticationFailureHandler(failureHandler);
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
}


