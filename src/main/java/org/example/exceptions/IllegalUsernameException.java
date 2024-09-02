package org.example.exceptions;

public class IllegalUsernameException extends CustomRuntimeException{
    public IllegalUsernameException(String username){
        super("Illegal username: " + username);
    }
}
