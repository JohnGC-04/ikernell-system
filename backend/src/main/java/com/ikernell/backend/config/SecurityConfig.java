package com.ikernell.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Permitimos usuarios Y proyectos temporalmente para pruebas
                        .requestMatchers("/api/usuarios/**").permitAll()
                        .requestMatchers("/api/proyectos/**").permitAll() // permite todas las rutas de proyectos, sin autenticación
                        .requestMatchers("/api/etapas/**").permitAll()
                        .requestMatchers("/api/etapas/**").permitAll()
                        .requestMatchers("/api/actividades/**").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }
}