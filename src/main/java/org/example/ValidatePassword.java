package org.example;

import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ValidatePassword {
    private final Logger logger = LoggerFactory.getLogger(ValidatePassword.class);


    /**
     * Password validation method.
     */
    public boolean passwordNotValid(String password) {
        logger.debug("Checking if password: " + password + " is valid");
        if (password == null) {
            return true;
        }
        String patternToMatch = "^[a-zA-Z0-9@#$%^&+-=!]{10}$";
        Pattern pattern = Pattern.compile(patternToMatch);
        return !pattern.matcher(password).matches();
    }
}
