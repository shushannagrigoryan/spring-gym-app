package org.example.services;

import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidatePassword {
    /**
     * Password validation method.
     */
    public boolean passwordNotValid(String password) {
        log.debug("Checking if password: {} is valid", password);
        if (password == null) {
            return true;
        }
        //String patternToMatch = "^[a-zA-Z0-9@#$%^&+-=!]{10}$";
        String patternToMatch = "^[a-zA-Z0-9`@#$%^&~!\\-*() \"'_+\\[\\]{}|;:,./<>?=-\\\\]{10}$";
        Pattern pattern = Pattern.compile(patternToMatch);
        return !pattern.matcher(password).matches();
    }
}
