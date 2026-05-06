package com.ikernell.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import com.ikernell.backend.security.JwtFilter;

import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 1. Rutas Públicas
                        .requestMatchers("/api/auth/**").permitAll()

                        // 2. Reglas de Roles (De lo más específico a lo más general)
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll() // Registro abierto
                        .requestMatchers("/api/usuarios/**").hasRole("COORDINADOR")

                        .requestMatchers("/api/proyectos/**").hasAnyRole("LIDER", "COORDINADOR")
                        .requestMatchers("/api/etapas/**").hasAnyRole("LIDER", "COORDINADOR")

                        // Los tres roles pueden interactuar con actividades (según sus métodos)
                        .requestMatchers("/api/actividades/**").hasAnyRole("LIDER", "COORDINADOR", "DESARROLLADOR")

                        // 4. Permitir acceso a Swagger UI sin autenticación
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll()

                        // 3. Bloqueo total para cualquier otra ruta
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}