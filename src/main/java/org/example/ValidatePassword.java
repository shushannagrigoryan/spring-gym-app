package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class ValidatePassword {
    private static final Logger logger = LoggerFactory.getLogger(ValidatePassword.class);
    private static final String PATTERN = "^[a-zA-Z0-9@#$%^&+-=!]{10}$";


    public static boolean passwordNotValid(String password){
        logger.debug("Checking if password: " + password +" id valid");
        if(password == null){
            return true;
        }
        Pattern pattern = Pattern.compile(PATTERN);
        return !pattern.matcher(password).matches();
    }
}
