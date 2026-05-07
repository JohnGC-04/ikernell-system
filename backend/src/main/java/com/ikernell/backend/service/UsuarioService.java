package com.ikernell.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ikernell.backend.dto.UsuarioDTO;
import com.ikernell.backend.entity.Usuario;
import com.ikernell.backend.repository.UsuarioRepository;

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
        // 1. Validar duplicados
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado en el sistema");
        }

        // 2. Encriptación
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // 3. El estado inicial por defecto si no viene (Buena práctica)
        if (usuario.getEstado() == null) {
            usuario.setEstado("ACTIVO");
        }

        // 4. PERSISTENCIA: Al guardar, JPA usará el @Enumerated(EnumType.STRING)
        // que pusimos en la entidad Usuario.
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
        dto.setRol(u.getRol().name());
        dto.setEstado(u.getEstado());
        dto.setDireccion(u.getDireccion());
        dto.setTelefono(u.getTelefono());
        dto.setFechaNacimiento(u.getFechaNacimiento());
        dto.setFoto(u.getFoto());
        dto.setPerfilProfesional(u.getPerfilProfesional());
        dto.setEspecialidad(u.getEspecialidad());
        if (u.getRol() != null) {
            dto.setRol(u.getRol().name());
        }
        return dto;
    }
}