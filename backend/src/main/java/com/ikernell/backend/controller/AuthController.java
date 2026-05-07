package com.ikernell.backend.controller;

import com.ikernell.backend.dto.LoginRequest;
import com.ikernell.backend.entity.Usuario;
import com.ikernell.backend.repository.UsuarioRepository;
import com.ikernell.backend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
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
    public Map<String, String> login(@RequestBody LoginRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // VALIDACIÓN ADICIONAL: ¿Está activo?
        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            throw new RuntimeException("El usuario se encuentra inhabilitado");
        }

        if (passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword())) {
            // Al pasar el objeto 'usuario', jwtUtil podrá hacer usuario.getRol().name()
            String token = jwtUtil.generateToken(usuario);
            return Map.of("token", token);
        } else {
            throw new RuntimeException("Credenciales incorrectas");
        }
    }
}