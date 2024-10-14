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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptionhandlers.ExceptionResponse;
import org.example.exceptions.GymAuthenticationException;
import org.slf4j.MDC;
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
    private List<String> excludedUrls;


    @Override
    public void init(FilterConfig filterConfig) {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext());
        this.userAuth = ctx.getBean(UserAuth.class);

        String excludedUrlsParam = filterConfig.getInitParameter("excludedUrls");
        excludedUrls = Arrays.asList(excludedUrlsParam.split(","));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean isExcluded = excludedUrls.stream().anyMatch(httpRequest.getRequestURI()::endsWith);
        if (isExcluded) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String transactionId = UUID.randomUUID().toString();
            MDC.put("transactionId", transactionId);
            log.debug("Authenticating user.");

            String username = httpRequest.getHeader("username");
            String password = httpRequest.getHeader("password");

            userAuth.userAuth(username, password);
        } catch (GymAuthenticationException e) {
            httpResponse.setContentType("application/json");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = objectWriter.writeValueAsString(
                    new ExceptionResponse("Authentication failed: "
                    + e.getMessage(), HttpStatus.UNAUTHORIZED));
            httpResponse.getWriter().print(json);
            log.debug("Authentication failed: {}", e.getMessage());
            log.debug("Status Code: {}", HttpStatus.UNAUTHORIZED);
            //throw e;
            return;
        } finally {
            MDC.clear();
        }
        log.debug("Authentication succeeded.");

        chain.doFilter(request, response);
    }


}
