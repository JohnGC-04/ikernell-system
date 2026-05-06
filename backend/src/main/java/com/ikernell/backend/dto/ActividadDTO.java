package com.ikernell.backend.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ActividadDTO {
    private Long idActividad; 
    private String descripcion; 
    private String estado; // "PENDIENTE", "EN_PROCESO", "COMPLETADA"
    private Double costoEstimado; // Añadido para el guardado
    private Long idEtapa; // Añadido para el guardado
    private Long idDesarrollador; // Añadido para el guardado
    private Long idProyecto; // Escencial para la validación
    private String nombreEtapa; // Añadido para la respuesta
    private String nombreDesarrollador; // Añadido para la respuesta
    private LocalDateTime fechaEntregaPlaneada; // Añadido para el guardado
}