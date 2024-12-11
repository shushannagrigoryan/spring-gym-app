package org.example.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtEncodingException;

@Slf4j
@RequiredArgsConstructor
public class JwtCustomEncoder implements JwtEncoder {
    private final JwtEncoder jwtEncoder;

    @Override
    public Jwt encode(JwtEncoderParameters parameters) throws JwtEncodingException {
        log.debug("Running custom jwtEncoder.");
        return jwtEncoder.encode(parameters);
    }
}
