package com.example.maintenance.config;

import com.example.maintenance.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
private final JwtService jwtService;
public SecurityConfig(JwtService jwtService)
{
    this.jwtService=jwtService;
}
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter()
    {
        return new JwtAuthenticationFilter(jwtService);
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .exceptionHandling(exception ->
//                        exception.authenticationEntryPoint(
//                                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
//                        ))
                .authorizeHttpRequests(auth -> auth

                        // Public endpoints
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/auth/login").permitAll()

                        // Read issues
                        .requestMatchers(HttpMethod.GET, "/api/issues/**").authenticated()

                        // Create issues
                        .requestMatchers(HttpMethod.POST, "/api/issues").hasAnyRole("USER","TECHNICIAN", "MANAGER", "ADMIN")


                        // Update issues
                        .requestMatchers(HttpMethod.PUT, "/api/issues/**").hasAnyRole("USER","TECHNICIAN", "MANAGER", "ADMIN")

                        // Delete issues
                        .requestMatchers(HttpMethod.DELETE, "/api/issues/**").hasAnyRole("USER","ADMIN")

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
