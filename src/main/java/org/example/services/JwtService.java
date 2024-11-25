package org.example.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.EntityNotFoundException;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TokenEntity;
import org.example.entity.UserEntity;
import org.example.repositories.TokenRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtService {
    private static final String SECRET_KEY =
        "FCj633yv9QJ57lcxB2jmDFvQAyR8dX4esyUYw1L8u2tNZoA2yE3J3azpQB7F4Agt";
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 5;
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60;
    private final TokenRepository tokenRepository;

    /**
     * Setting dependencies.
     */
    public JwtService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String getUsernameFromJwt(String jwtToken) {
        log.debug("JwtToken = {}", jwtToken);
        return extractClaim(jwtToken, Claims::getSubject);
    }

    private Claims extractAllClaims(String jwtToken) {
        log.debug("Extracting all claims from the token");
        return Jwts.parserBuilder().setSigningKey(getSigningKey())
            .build()
            .parseClaimsJws(jwtToken)
            .getBody();
    }

    private Key getSigningKey() {
        log.debug("Getting the signing key for jwt token.");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts a specific claim from the given token.
     *
     * @param jwtToken       the jwt token form which the claim should be extracted
     * @param claimsResolver a {@code Function} for getting a specific claim from all claims
     * @return the extracted claim
     */
    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        log.debug("Extracting a single claim.");
        Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    /**
     * Generating access token.
     *
     * @param extraClaims extra claims to be added to the jwt payload.
     * @param userDetails user for which the jwt is generated.
     * @return {@code String} jwt access token
     */
    public String generateAccessToken(Map<String, Object> extraClaims,
                                      UserDetails userDetails) {
        log.debug("Generating access token for user: {}", userDetails.getUsername());
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Generate refresh token.
     */
    public String generateRefreshToken(Map<String, Object> extraClaims,
                                       UserDetails userDetails) {
        log.debug("Generating refresh token for user: {}", userDetails.getUsername());
        return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Checks if the given token is valid or not.
     *
     * @param jwtToken    the jwt token
     * @param userDetails user for whom the jwt token was generated
     * @return {@code boolean} true is the token is valid, otherwise false
     */
    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        log.debug("Checking if the token id valid.");
        String username = getUsernameFromJwt(jwtToken);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    }

    private boolean isTokenExpired(String jwtToken) {
        log.debug("Checking if the given token {} is expired.", jwtToken);
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        log.debug("Getting expiration date for the given token: {}", jwtToken);
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    public void saveGeneratedToken(TokenEntity jwtToken) {
        log.debug("Saving jwtToken in database.");
        tokenRepository.save(jwtToken);
    }

    /**
     * Revoking user's active tokens.
     *
     * @param user user for whose tokens should be revoked.
     */
    public void revokeAllUserTokens(UserEntity user) {
        log.debug("Revoking user's all active tokens.");
        List<TokenEntity> activeTokens = tokenRepository
            .findByUserAndRevoked(user, false);
        if (activeTokens.isEmpty()) {
            return;
        }

        activeTokens.forEach(token -> token.setRevoked(true));
        tokenRepository.saveAll(activeTokens);
    }

    /**
     * Returns true if the token is revoked, otherwise false.
     *
     * @param jwtToken jwt token
     */
    public boolean isTokenRevoked(String jwtToken) {
        log.debug("Checking if the given token is not expired and not revoked.");
        TokenEntity token = tokenRepository.findByToken(jwtToken).orElseThrow(
            () -> new EntityNotFoundException("No token found.")
        );
        return token.isRevoked();
    }
}
