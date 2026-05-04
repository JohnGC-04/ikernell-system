package com.ikernell.backend.service;

import com.ikernell.backend.dto.ActividadDTO;
import com.ikernell.backend.entity.Actividad;
import com.ikernell.backend.entity.Etapa;
import com.ikernell.backend.entity.Proyecto;
import com.ikernell.backend.entity.Usuario;
import com.ikernell.backend.exception.PresupuestoExcedidoException;
import com.ikernell.backend.repository.ActividadRepository;
import com.ikernell.backend.repository.EtapaRepository;
import com.ikernell.backend.repository.ProyectoRepository;
import com.ikernell.backend.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;


@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final EtapaRepository etapaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProyectoRepository proyectoRepository; // <--- 1. Declarar

    public ActividadService(ActividadRepository actividadRepository, EtapaRepository etapaRepository,
            UsuarioRepository usuarioRepository, ProyectoRepository proyectoRepository) {
        this.actividadRepository = actividadRepository;
        this.etapaRepository = etapaRepository;
        this.usuarioRepository = usuarioRepository;
        this.proyectoRepository = proyectoRepository; // <--- 2. Inicializar
    }

    public ActividadDTO guardarActividad(ActividadDTO dto) {
        // Buscar el proyecto para validar presupuesto
        Proyecto proyecto = proyectoRepository.findById(dto.getIdProyecto())
                .orElseThrow(() -> new RuntimeException("Proyecto no encontrado"));

        double costoActual = proyecto.getEtapas().stream()
                .flatMap(etapa -> etapa.getActividades().stream())
                .mapToDouble(Actividad::getCostoEstimado)
                .sum();

        if (costoActual + dto.getCostoEstimado() > proyecto.getPresupuesto()) {
            throw new PresupuestoExcedidoException("Presupuesto insuficiente. Restante: $" + (proyecto.getPresupuesto() - costoActual));

        }

        //Convertir y guardar la actividad
        Actividad actividad = convertirAEntidad(dto);
        return convertirADto(actividadRepository.save(actividad));
}

// 3. Método para convertir DTO a Entidad (Soluciona el error de undefined)
    private Actividad convertirAEntidad(ActividadDTO dto) {
        Actividad a = new Actividad();
        a.setDescripcion(dto.getDescripcion());
        a.setEstado(dto.getEstado());
        a.setCostoEstimado(dto.getCostoEstimado());
        
        Etapa etapa = etapaRepository.findById(dto.getIdEtapa())
                .orElseThrow(() -> new RuntimeException("Etapa no encontrada"));
        Usuario dev = usuarioRepository.findById(dto.getIdDesarrollador())
                .orElseThrow(() -> new RuntimeException("Desarrollador no encontrado"));
                
        a.setEtapa(etapa);
        a.setDesarrollador(dev);
        return a;
    }

    public ActividadDTO guardar(Actividad actividad) {
        Etapa etapa = etapaRepository.findById(actividad.getEtapa().getIdEtapa())
                .orElseThrow(() -> new RuntimeException("Etapa no encontrada"));

        Usuario dev = usuarioRepository.findById(actividad.getDesarrollador().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Desarrollador no encontrado"));

        actividad.setEtapa(etapa);
        actividad.setDesarrollador(dev);

        Actividad guardada = actividadRepository.save(actividad);
        return convertirADto(guardada);
    }

    public List<ActividadDTO> listarPorEtapa(Long idEtapa) {
        return actividadRepository.findByEtapaIdEtapa(idEtapa)
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    public List<ActividadDTO> listarTodas() {
        return actividadRepository.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    private ActividadDTO convertirADto(Actividad a) {
        ActividadDTO dto = new ActividadDTO();
        dto.setIdActividad(a.getIdActividad());
        dto.setDescripcion(a.getDescripcion());
        dto.setEstado(a.getEstado());
        dto.setCostoEstimado(a.getCostoEstimado());
        dto.setNombreEtapa(a.getEtapa().getNombreEtapa());
        dto.setNombreDesarrollador(a.getDesarrollador().getNombre() + " " + a.getDesarrollador().getApellido());
        return dto;
    }

    public List<ActividadDTO> listarPorEmailDesarrollador(String email) {
        return actividadRepository.findByDesarrolladorEmail(email)
                .stream()
                .map(this::convertirADto) // Reutiliza tu método de conversión
                .collect(Collectors.toList());
    }


}