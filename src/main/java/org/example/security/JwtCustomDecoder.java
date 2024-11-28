package org.example.security;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtCustomDecoder {
    @Bean
    public JwtDecoder jwtDecoder() {
        String secret = "FCj633yv9QJ57lcxB2jmDFvQAyR8dX4esyUYw1L8u2tNZoA2yE3J3azpQB7F4Agt";
        SecretKey originalKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(originalKey).build();
        return jwtDecoder;
    }


}
