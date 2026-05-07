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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilitar CORS con la configuración definida
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html")
                        .permitAll() // Swagger abierto
                        .requestMatchers("/api/usuarios/**").hasRole("COORDINADOR")
                        .requestMatchers("/api/proyectos/**").hasAnyRole("LIDER", "COORDINADOR")
                        .requestMatchers("/api/etapas/**").hasAnyRole("LIDER", "COORDINADOR")
                        .requestMatchers("/api/actividades/**").hasAnyRole("LIDER", "COORDINADOR", "DESARROLLADOR")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Agregar el filtro JWT antes del filtro de autenticación

        return http.build();
    }

    // Bean para permitir peticiones desde el Frontend (Svelte)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() { // Configuración CORS personalizada
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Puerto por defecto de Vite/Svelte
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));// Permitir el header de autorización para JWT
        configuration.setAllowCredentials(true); // Permitir cookies (si es necesario)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); //  Registrar la configuración CORS para todas las rutas
        source.registerCorsConfiguration("/**", configuration); // Aplicar esta configuración a todas las rutas
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // Bean para encriptar contraseñas
        return new BCryptPasswordEncoder(); // Usamos BCrypt para hashing de contraseñas
    }
}