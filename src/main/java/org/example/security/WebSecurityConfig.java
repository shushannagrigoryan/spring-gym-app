package org.example.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;

    /**
     * Setting dependencies.
     */
    public WebSecurityConfig(AuthenticationProvider authenticationProvider,
                             CustomAuthenticationSuccessHandler authenticationSuccessHandler,
                             CustomAuthenticationFailureHandler authenticationFailureHandler,
                             CustomAuthenticationEntryPoint authenticationEntryPoint,
                             CustomLogoutSuccessHandler logoutSuccessHandler) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    /**
     * Configuring security filter chain.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/trainees", "/trainers").permitAll()
                .anyRequest().authenticated())
            .authenticationProvider(authenticationProvider)
            .formLogin(login -> login
                .permitAll()
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
            )
            .exceptionHandling(e -> e.authenticationEntryPoint(authenticationEntryPoint))
            .logout(logout -> logout
                .permitAll()
                .logoutSuccessHandler(logoutSuccessHandler));
        return http.build();
    }


    /**
     * Authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
