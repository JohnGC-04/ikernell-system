package com.ikernell.backend.service;

import com.ikernell.backend.dto.ProyectoDTO;
import com.ikernell.backend.entity.Actividad;
import com.ikernell.backend.entity.Etapa;
import com.ikernell.backend.entity.Proyecto;
import com.ikernell.backend.entity.Usuario;
import com.ikernell.backend.repository.ProyectoRepository;
import com.ikernell.backend.repository.UsuarioRepository;
import com.ikernell.backend.repository.EtapaRepository;
import com.ikernell.backend.repository.ActividadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EtapaRepository etapaRepository;
    private final ActividadRepository actividadRepository;

    public ProyectoService(ProyectoRepository proyectoRepository, UsuarioRepository usuarioRepository,
            EtapaRepository etapaRepository, ActividadRepository actividadRepository) {
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
        this.etapaRepository = etapaRepository;// Inyección de dependencias para ActividadRepository
        this.actividadRepository = actividadRepository;// Inyección de dependencias para ActividadRepository
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

    public Double calcularCostoTotal(Long idProyecto) {
        // 1. Buscamos las etapas asociadas al proyecto
        List<Etapa> etapas = etapaRepository.findByProyectoIdProyecto(idProyecto);

        // 2. Procesamos el cálculo con tipos claros
        return etapas.stream()
                .flatMap(etapa -> {
                    // Obtenemos la lista de actividades para esta etapa
                    List<Actividad> actividades = actividadRepository.findByEtapaIdEtapa(etapa.getIdEtapa());
                    // La convertimos explícitamente a Stream
                    return actividades.stream();
                })
                .mapToDouble(actividad -> {
                    // Validamos nulos para evitar errores en tiempo de ejecución
                    return actividad.getCostoEstimado() != null ? actividad.getCostoEstimado() : 0.0;
                })
                .sum();
    }

    private ProyectoDTO convertirADto(Proyecto p) {
        // Mapeo manual de campos del proyecto
        ProyectoDTO dto = new ProyectoDTO();
        dto.setIdProyecto(p.getIdProyecto());
        dto.setNombre(p.getNombre());
        dto.setDescripcion(p.getDescripcion());
        dto.setFechaInicio(p.getFechaInicio());
        dto.setFechaFin(p.getFechaFin());
        dto.setEstado(p.getEstado());

        // Cálculo del costo total del proyecto
        dto.setCostoTotal(this.calcularCostoTotal(p.getIdProyecto()));
        // Mapeo manual de datos del líder
        if (p.getLider() != null) {
            dto.setIdLider(p.getLider().getIdUsuario());
            dto.setNombreLider(p.getLider().getNombre() + " " + p.getLider().getApellido());
        }
        return dto;
    }
}