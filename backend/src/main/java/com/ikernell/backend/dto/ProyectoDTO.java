package com.ikernell.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProyectoDTO {
    private Long idProyecto;
    private String nombre;
    private String descripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    
    // Datos simplificados del Líder (HU-003)
    private Long idLider;
    private String nombreLider;

    // Costo total del proyecto (suma de costos estimados de actividades)
    private Double costoTotal;
}