package org.example.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;


@RequiredArgsConstructor
@Slf4j
public class JwtCustomDecoder implements JwtDecoder {
    private final JwtDecoder jwtDecoder;

    @Override
    public Jwt decode(String token) throws JwtException {
        log.debug("Running custom jwt decoder.");
        log.debug("token = {}", token);
        return jwtDecoder.decode(token);
    }
}
