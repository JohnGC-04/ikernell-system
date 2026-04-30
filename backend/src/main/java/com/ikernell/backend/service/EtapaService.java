package com.ikernell.backend.service;

import com.ikernell.backend.dto.EtapaDTO;
import com.ikernell.backend.entity.Etapa;
import com.ikernell.backend.entity.Proyecto;
import com.ikernell.backend.repository.EtapaRepository;
import com.ikernell.backend.repository.ProyectoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EtapaService {

    private final EtapaRepository etapaRepository;
    private final ProyectoRepository proyectoRepository;

    public EtapaService(EtapaRepository etapaRepository, ProyectoRepository proyectoRepository) {
        this.etapaRepository = etapaRepository;
        this.proyectoRepository = proyectoRepository;
    }

    public EtapaDTO guardar(Etapa etapa) {
        if (etapa.getProyecto() == null || etapa.getProyecto().getIdProyecto() == null) {
            throw new RuntimeException("La etapa debe estar vinculada a un proyecto");
        }

        Proyecto proyecto = proyectoRepository.findById(etapa.getProyecto().getIdProyecto())
                .orElseThrow(() -> new RuntimeException("El proyecto no existe"));
        
        etapa.setProyecto(proyecto);
        Etapa guardada = etapaRepository.save(etapa);
        return convertirADto(guardada);
    }

    public List<EtapaDTO> listarPorProyecto(Long idProyecto) {
        return etapaRepository.findByProyectoIdProyecto(idProyecto)
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    private EtapaDTO convertirADto(Etapa e) {
        EtapaDTO dto = new EtapaDTO();
        dto.setIdEtapa(e.getIdEtapa());
        dto.setNombreEtapa(e.getNombreEtapa());
        dto.setDescripcion(e.getDescripcion());
        dto.setIdProyecto(e.getProyecto().getIdProyecto());
        dto.setNombreProyecto(e.getProyecto().getNombre());
        return dto;
    }
}