package com.ikernell.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProyectoDTO {

    private Long idProyecto;

    @NotBlank(message = "El nombre del proyecto es obligatorio")
    @Size(min = 5, max = 100, message = "El nombre debe tener entre 5 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    private String descripcion;

    @NotNull(message = "El presupuesto es obligatorio")
    @DecimalMin(value = "0.1", message = "El presupuesto debe ser un valor positivo")
    private Double presupuesto;

    @NotBlank(message = "El estado inicial es obligatorio")
    @Pattern(regexp = "Pendiente|En Progreso|Completado", message = "Estado de proyecto no válido")
    private String estado;

    @NotNull(message = "Debe asignar un líder al proyecto")
    private Long idLider;


    // Campos de solo lectura para la respuesta
    private LocalDateTime fechaFinReal;
    private String nombreLider;

    private Double costoTotal; // Costo total del proyecto (suma de costos estimados de actividades)
    private String categoria; // Ej: "Desarrollo de Software", "Construcción", "Investigación"
    private LocalDateTime fechaInicio; // Fecha de inicio planeada
    private LocalDateTime fechaFin; // Fecha de fin planeada

}






/* package com.ikernell.backend.dto;

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
} */