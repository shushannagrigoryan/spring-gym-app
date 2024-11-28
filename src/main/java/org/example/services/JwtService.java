package org.example.services;

import com.nimbusds.jose.JOSEException;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.TokenEntity;
import org.example.entity.UserEntity;
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
    private static final String SECRET_KEY =
        "FCj633yv9QJ57lcxB2jmDFvQAyR8dX4esyUYw1L8u2tNZoA2yE3J3azpQB7F4Agt";
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

//    public String getUsernameFromJwt(String jwtToken) {
//        log.debug("JwtToken = {}", jwtToken);
//        return extractClaim(jwtToken, Claims::getSubject);
//    }

//    private Claims extractAllClaims(String jwtToken) {
//        log.debug("Extracting all claims from the token");
//        return Jwts.parserBuilder().setSigningKey(getSigningKey())
//            .build()
//            .parseClaimsJws(jwtToken)
//            .getBody();
//    }

//    private Key getSigningKey() {
//        log.debug("Getting the signing key for jwt token.");
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
    public Jwt decodeToken(String token) {
        log.debug("Decode token.");
//        return jwtDecoder.jwtDecoder(decode(token);
        return jwtDecoder.jwtDecoder().decode(token);
    }

//    /**
//     * Extracts a specific claim from the given token.
//     *
//     * @param jwtToken       the jwt token form which the claim should be extracted
//     * @param claimsResolver a {@code Function} for getting a specific claim from all claims
//     * @return the extracted claim
//     */
//    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
//        log.debug("Extracting a single claim.");
//        Claims claims = extractAllClaims(jwtToken);
//        return claimsResolver.apply(claims);
//    }

//    /**
//     * Generating access token.
//     *
//     * @param extraClaims extra claims to be added to the jwt payload.
//     * @param userDetails user for which the jwt is generated.
//     * @return {@code String} jwt access token
//     */
//    public String generateAccessToken(Map<String, Object> extraClaims,
//                                      UserDetails userDetails) {
//        log.debug("Generating access token for user: {}", userDetails.getUsername());
//        return Jwts.builder()
//            .setClaims(extraClaims)
//            .setSubject(userDetails.getUsername())
//            .setIssuedAt(new Date(System.currentTimeMillis()))
//            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
//            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//            .compact();
//    }



    //    /**
    //     * Checks if the given token is valid or not.
    //     *
    //     * @param jwtToken    the jwt token
    //     * @param userDetails user for whom the jwt token was generated
    //     * @return {@code boolean} true is the token is valid, otherwise false
    //     */
    //    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
    //        log.debug("Checking if the token id valid.");
    //        String username = getUsernameFromJwt(jwtToken);
    //        return (username.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    //    }

    //    private boolean isTokenExpired(String jwtToken) {
    //        log.debug("Checking if the given token {} is expired.", jwtToken);
    //        return extractExpiration(jwtToken).before(new Date());
    //    }

    //    private Date extractExpiration(String jwtToken) {
    //        log.debug("Getting expiration date for the given token: {}", jwtToken);
    //        return extractClaim(jwtToken, Claims::getExpiration);
    //    }

    public void saveGeneratedToken(TokenEntity jwtToken) {
        log.debug("Saving jwtToken in database.");
        tokenRepository.save(jwtToken);
        log.debug("Successfully saved the generated jwt token in database.");
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

        public List<TokenEntity> findNonRevokedTokensByUser(String username) {
            log.debug("Getting non revoked tokens of user {}.", username);
            return tokenRepository.findByUser_UsernameAndRevoked(username, false);
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
