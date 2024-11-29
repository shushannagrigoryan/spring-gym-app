//package org.example.security;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.services.JwtService;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class CustomJwtAuthenticationProvider implements AuthenticationProvider {
//    private final JwtService jwtService;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        log.debug("Running the authenticate method in AuthenticationProvider");
//        String name = authentication.getName();
//        System.out.println("NAME = " + name);
//        Jwt jwt = jwtService.decodeToken(name);
//        System.out.println("CLAIMS");
//        System.out.println(jwt.getClaims());
//        jwt.getClaims().forEach((claim, c) -> {
//            System.out.println(claim);
//            System.out.println(c);
//        });
//        log.debug("Username = {}", jwt.getSubject());
//        authentication.getAuthorities().forEach(System.out::println);
//        authentication.setAuthenticated(true);
//        return authentication;
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        log.debug("Supports method in custom authentication provider.");
//        System.out.println("JwtAuthenticationToken.class.isAssignableFrom(authentication) = "
//            + JwtAuthenticationToken.class.isAssignableFrom(authentication));
//        System.out.println("BearerTokenAuthenticationToken.class.isAssignableFrom(authentication) = "
//        + BearerTokenAuthenticationToken.class.isAssignableFrom(authentication));
//        return BearerTokenAuthenticationToken.class.isAssignableFrom(authentication);
//    }
//}
