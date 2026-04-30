package com.ikernell.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ikernell.backend.dto.UsuarioDTO;
import com.ikernell.backend.entity.Usuario;
import com.ikernell.backend.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirADto)
                .toList();
    }

    public UsuarioDTO guardar(Usuario usuario) {
        // Lógica de negocio: Validar si el email ya existe antes de guardar
        if(usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado"); 
        }
        
        Usuario guardado = usuarioRepository.save(usuario);
        return convertirADto(guardado);
    }

    // Método privado para no repetir código (DRY - Don't Repeat Yourself)
    private UsuarioDTO convertirADto(Usuario u) {
    UsuarioDTO dto = new UsuarioDTO();
    dto.setIdUsuario(u.getIdUsuario());
    dto.setNombre(u.getNombre());
    dto.setApellido(u.getApellido());
    dto.setEmail(u.getEmail());
    dto.setIdentificacion(u.getIdentificacion());
    // Asegúrate de que el tipo de dato coincida (String o RolUsuario)
    dto.setRol(u.getRol() != null ? u.getRol().toString() : null); 
    dto.setEstado(u.getEstado() != null ? u.getEstado().toString() : null);
    return dto;
}
}