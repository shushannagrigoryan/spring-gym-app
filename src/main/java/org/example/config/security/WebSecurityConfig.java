package org.example.config.security;

import lombok.RequiredArgsConstructor;
import org.example.security.CustomAccessDeniedHandler;
import org.example.security.CustomAuthenticationEntryPoint;
import org.example.security.JwtAuthConverter;
import org.example.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final SecurityConfig securityConfig;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final JwtAuthFilter jwtAuthFilter;
    private final JwtAuthConverter jwtAuthConverter;
    private final CustomAccessDeniedHandler accessDeniesHandler;
    private final LogoutHandler logoutHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;

    /**
     * Configuring security filter chain.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(securityConfig.corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .oauth2ResourceServer(oauth -> oauth
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter))
                .authenticationEntryPoint(authenticationEntryPoint))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.POST, "/trainees", "/trainers").permitAll()
                .anyRequest().authenticated())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(e -> e
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniesHandler))
            .logout(logout -> logout.permitAll()
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler(logoutSuccessHandler))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
