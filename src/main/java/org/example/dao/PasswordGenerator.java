package org.example.dao;

import java.security.SecureRandom;

public class PasswordGenerator {
    /** Generating random 10 length password. */
    public String generatePassword() {
        StringBuilder stringBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < 10; i++) {
            stringBuilder.append((char) secureRandom.nextInt(32, 127));
        }
        return stringBuilder.toString();
    }
}
