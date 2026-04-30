package com.ikernell.backend.service;

import com.ikernell.backend.dto.UsuarioDTO;
import com.ikernell.backend.entity.Usuario;
import com.ikernell.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public UsuarioDTO guardar(Usuario usuario) {
        // Validar si el email ya existe (RF-010 / HU-011)
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado en el sistema");
        }
        
        // ENCRIPTACIÓN AQUÍ
        String encodedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(encodedPassword);

        // El estado se recibe como String desde el controlador (ej: "ACTIVO")
        Usuario guardado = usuarioRepository.save(usuario);
        return convertirADto(guardado);
    }

    private UsuarioDTO convertirADto(Usuario u) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(u.getIdUsuario());
        dto.setNombre(u.getNombre());
        dto.setApellido(u.getApellido());
        dto.setEmail(u.getEmail());
        dto.setIdentificacion(u.getIdentificacion());
        dto.setRol(u.getRol());
        dto.setEstado(u.getEstado());
        dto.setDireccion(u.getDireccion());
        dto.setTelefono(u.getTelefono());
        dto.setFechaNacimiento(u.getFechaNacimiento());
        dto.setFoto(u.getFoto());
        dto.setPerfilProfesional(u.getPerfilProfesional());
        dto.setEspecialidad(u.getEspecialidad());
        return dto;
    }
}