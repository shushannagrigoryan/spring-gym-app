package org.example.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptionhandlers.ExceptionResponse;
import org.example.exceptions.GymAuthenticationException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

@Slf4j
@Component
@WebFilter(urlPatterns = {"/trainees/*", "/trainers/*", "/users/*", "/training-types/*", "/trainings/*"},
        initParams = {@WebInitParam(name = "excludedUrls", value = "/trainees/register,/trainers/register")})
public class AuthFilter implements Filter {
    private UserAuth userAuth;

    @Override
    public void init(FilterConfig filterConfig) {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext());
        this.userAuth = ctx.getBean(UserAuth.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.debug("Authenticating user.");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String username = httpRequest.getHeader("username");
        String password = httpRequest.getHeader("password");

        try {
            userAuth.userAuth(username, password);
        } catch (GymAuthenticationException e) {
            httpResponse.setContentType("application/json");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = objectWriter.writeValueAsString(
                    new ExceptionResponse("Authentication failed: " + e.getMessage(), HttpStatus.UNAUTHORIZED));
            httpResponse.getWriter().print(json);
            return;
        }

        chain.doFilter(request, response);
    }


}
