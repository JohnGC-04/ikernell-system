package com.ikernell.backend.service;

import com.ikernell.backend.dto.ActividadDTO;
import com.ikernell.backend.entity.Actividad;
import com.ikernell.backend.entity.Etapa;
import com.ikernell.backend.entity.Usuario;
import com.ikernell.backend.repository.ActividadRepository;
import com.ikernell.backend.repository.EtapaRepository;
import com.ikernell.backend.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final EtapaRepository etapaRepository;
    private final UsuarioRepository usuarioRepository;

    public ActividadService(ActividadRepository actividadRepository, EtapaRepository etapaRepository,
            UsuarioRepository usuarioRepository) {
        this.actividadRepository = actividadRepository;
        this.etapaRepository = etapaRepository;
        this.usuarioRepository = usuarioRepository;
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
}