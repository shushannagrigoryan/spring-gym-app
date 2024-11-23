package org.example.authorizationvalidators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizeUserByUsername {
    /**
     * Checks if the authenticated user username and the given username are the same.
     */
    public boolean isAuthorized(String username) {
        log.debug("Checking if the given username and the authenticated username are the same.");
        String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return authenticatedUsername.equals(username);
    }
}
