package com.ikernell.backend.dto;

import lombok.Data;

@Data
public class ActividadDTO {
    private Long idActividad;
    private String descripcion;
    private String estado;
    private Double costoEstimado;
    private Long idEtapa; // Añadido para el guardado
    private Long idDesarrollador; // Añadido para el guardado
    private Long idProyecto; // Escencial para la validación
    private String nombreEtapa;
    private String nombreDesarrollador;
}