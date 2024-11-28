package org.example.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtAuthConverter implements Converter<Jwt, JwtAuthenticationToken> {
    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt source) {
        log.debug("Converting jwt token.");
        Collection<GrantedAuthority> authorities = extractAuthorities(source);
        return new JwtAuthenticationToken(source);
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        log.debug("Extracting authorities.");
        Map<String, Object> claims = jwt.getClaims();
        jwt.getClaims().forEach((claim, c) -> {
            System.out.println(claim);
            System.out.println(c.toString());

        });
        System.out.println("Printing claims");
        List<String> roles = (List<String>) claims.getOrDefault("roles", Collections.emptyList());


        // Convert roles into GrantedAuthority
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());
    }


}
