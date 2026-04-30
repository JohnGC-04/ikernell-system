package com.ikernell.backend.service;

import com.ikernell.backend.dto.ProyectoDTO;
import com.ikernell.backend.entity.Proyecto;
import com.ikernell.backend.entity.Usuario;
import com.ikernell.backend.repository.ProyectoRepository;
import com.ikernell.backend.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProyectoService(ProyectoRepository proyectoRepository, UsuarioRepository usuarioRepository) {
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ProyectoDTO guardar(Proyecto proyecto) {
        // 1. Validación de seguridad para evitar el NullPointerException
        if (proyecto.getLider() == null || proyecto.getLider().getIdUsuario() == null) {
            throw new RuntimeException("Debe asignar un Líder válido al proyecto");
        }

        // 2. Ahora sí buscamos al líder con seguridad
        Usuario lider = usuarioRepository.findById(proyecto.getLider().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("El Líder con ID " +
                        proyecto.getLider().getIdUsuario() + " no existe"));

        proyecto.setLider(lider);
        Proyecto guardado = proyectoRepository.save(proyecto);
        return convertirADto(guardado);
    }

    public List<ProyectoDTO> listarTodos() {
        return proyectoRepository.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    private ProyectoDTO convertirADto(Proyecto p) {
        ProyectoDTO dto = new ProyectoDTO();
        dto.setIdProyecto(p.getIdProyecto());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setFechaInicio(p.getFechaInicio());
        dto.setFechaFin(p.getFechaFin());
        dto.setEstado(p.getEstado());

        // Mapeo manual de datos del líder
        if (p.getLider() != null) {
            dto.setIdLider(p.getLider().getIdUsuario());
            dto.setNombreLider(p.getLider().getNombre() + " " + p.getLider().getApellido());
        }
        return dto;
    }
}