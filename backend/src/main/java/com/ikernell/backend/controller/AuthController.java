package com.ikernell.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikernell.backend.dto.LoginRequest;
import com.ikernell.backend.entity.Usuario;
import com.ikernell.backend.repository.UsuarioRepository;
import com.ikernell.backend.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173") // Permitir CORS para este controlador
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("--- INTENTO DE LOGIN ---");
        System.out.println("Email: [" + loginRequest.getEmail() + "]");
        System.out.println("Password enviada: [" + loginRequest.getPassword() + "]");

        return usuarioRepository.findByEmail(loginRequest.getEmail())
                .map(usuario -> {
                    System.out.println("Hash en BD: [" + usuario.getPassword() + "]");
                    // ... resto del código
                    if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
                        return ResponseEntity.status(401).body("Usuario inhabilitado");
                    }
                    if (passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
                        String token = jwtUtil.generateToken(usuario);
                        return ResponseEntity.ok(Map.of("token", token));
                    }
                    return ResponseEntity.status(401).body("Credenciales incorrectas");
                })
                .orElse(ResponseEntity.status(404).body("Usuario no encontrado"));
    }

}