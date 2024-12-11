//package org.example.security;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.example.entity.UserEntity;
//import org.example.exceptions.GymEntityNotFoundException;
//import org.example.services.LoginAttemptService;
//import org.example.services.UserService;
//import org.springframework.context.ApplicationListener;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@RequiredArgsConstructor
//public class AuthenticationFailureListener implements
//    ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
//    private final LoginAttemptService loginAttemptService;
//    private final UserService userService;
//
//    @Override
//    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
//        if (event.getSource() instanceof UsernamePasswordAuthenticationToken) {
//            String username = event.getAuthentication().getPrincipal().toString();
//            log.debug("Failed authentication due to bad credentials for user {}.", username);
//            UserEntity user = userService.getUserByUsername(username)
//                .orElseThrow(() -> new GymEntityNotFoundException("User is not found.")
//                );
//            loginAttemptService.loginFailed(user);
//        }
//    }
//}
