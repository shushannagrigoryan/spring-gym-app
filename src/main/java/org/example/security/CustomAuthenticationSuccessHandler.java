//package org.example.security;
//
//import jakarta.persistence.EntityNotFoundException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.entity.UserEntity;
//import org.example.services.GymUserDetailService;
//import org.example.services.UserService;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
////    private final JwtService jwtService;
//    private final GymUserDetailService userDetailsService;
//    private final UserService userService;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//        log.debug("Authentication success handler.");
//        String username = authentication.getName();
//        UserDetails user = userDetailsService.loadUserByUsername(username);
//
////        String jwtAccessToken = jwtService.generateAccessToken(Map.of("Type", TokenType.ACCESS), user);
////        String jwtRefreshToken = jwtService.generateRefreshToken(Map.of("Type", TokenType.REFRESH), user);
//
//        UserEntity userEntity = userService.getUserByUsername(username)
//            .orElseThrow(() -> new EntityNotFoundException("User not found"));
////        jwtService.saveGeneratedToken(new TokenEntity(jwtAccessToken, TokenType.ACCESS, false,
////            userEntity));
////        jwtService.saveGeneratedToken(new TokenEntity(jwtRefreshToken, TokenType.REFRESH, false,
////            userEntity));
//
//        response.setContentType("application/json");
////        response.getWriter().write("{\"AccessToken\": \"" + jwtAccessToken + "\"}");
////        response.getWriter().write("{\"RefreshToken\": \"" + jwtRefreshToken + "\"}");
//    }
//}