package org.example.services;

import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TokenEntity;
import org.example.repositories.TokenRepository;
import org.example.security.JwtCustomDecoder;
import org.example.security.JwtCustomEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 5;
    private final TokenRepository tokenRepository;
    private final JwtCustomDecoder jwtDecoder;
    private final JwtCustomEncoder jwtEncoder;

    /**
     * Setting dependencies.
     */
    public JwtService(TokenRepository tokenRepository,
                      JwtCustomDecoder jwtDecoder,
                      JwtCustomEncoder jwtEncoder) {
        this.tokenRepository = tokenRepository;
        this.jwtDecoder = jwtDecoder;
        this.jwtEncoder = jwtEncoder;
    }

    public Jwt decodeToken(String token) {
        log.debug("Decode token.");
        return jwtDecoder.jwtDecoder().decode(token);
    }

    /**
     * Saving the generated jwt token to database.
     */
    public void saveGeneratedToken(TokenEntity jwtToken) {
        log.debug("Saving jwtToken in database.");
        tokenRepository.save(jwtToken);
        log.debug("Successfully saved the generated jwt token in database.");
    }


    public List<TokenEntity> findNonRevokedTokensByUser(String username) {
        log.debug("Getting non revoked tokens of user {}.", username);
        return tokenRepository.findByUser_UsernameAndRevoked(username, false);
    }


    /**
     * Generates a jwt token using the Authentication object.
     */
    public String generateToken(Authentication authentication) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(authentication.getName())
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusMillis(ACCESS_TOKEN_EXPIRATION))
            .claim("authorities", authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList())
            .build();
        var encoder = jwtEncoder.jwtEncoder();
        JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
        String token = encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
        System.out.println("GENERATED TOKEN = " + token);
        return token;
    }
}
