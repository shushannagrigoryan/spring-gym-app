package org.example.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        log.debug("Logout success handler.");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        String jsonResponse = "{\"message\": \"You have successfully logged out.\"}";
        response.getWriter().write(jsonResponse);
    }
}
