package org.example;

import java.util.regex.Pattern;

public class ValidatePassword {
    private static final String PATTERN = "^[a-zA-Z0-9@#$%^&+-=!]{10}$";


    public static boolean isValidPassword(String password){
        if(password == null){
            return false;
        }
        Pattern pattern = Pattern.compile(PATTERN);
        return pattern.matcher(password).matches();
    }
}
