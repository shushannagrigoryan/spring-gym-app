package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;
@Component
public class ValidatePassword {
    private final Logger logger = LoggerFactory.getLogger(ValidatePassword.class);
    private final String PATTERN = "^[a-zA-Z0-9@#$%^&+-=!]{10}$";


    public  boolean passwordNotValid(String password){
        logger.debug("Checking if password: " + password +" is valid");
        if(password == null){
            return true;
        }
        Pattern pattern = Pattern.compile(PATTERN);
        return !pattern.matcher(password).matches();
    }
}
