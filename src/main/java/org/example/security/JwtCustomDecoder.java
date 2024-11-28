package org.example.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
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
        log.debug("Running the custom decoder");
        String secret = "FCj633yv9QJ57lcxB2jmDFvQAyR8dX4esyUYw1L8u2tNZoA2yE3J3azpQB7F4Agt";

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return NimbusJwtDecoder.withSecretKey(key).build();
    }


}
