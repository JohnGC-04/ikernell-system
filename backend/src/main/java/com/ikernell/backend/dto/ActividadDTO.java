package com.ikernell.backend.dto;

import lombok.Data;

@Data
public class ActividadDTO {
    private Long idActividad;
    private String descripcion;
    private String estado;
    private Double costoEstimado;
    private String nombreEtapa;
    private String nombreDesarrollador;
}