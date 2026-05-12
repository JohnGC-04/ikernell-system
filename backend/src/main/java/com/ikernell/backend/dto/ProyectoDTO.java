package com.ikernell.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
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
    private LocalDate fechaInicio; // Fecha de inicio planeada
    private LocalDate fechaFin; // Fecha de fin planeada

    private String estadoProyecto; // "PLANIFICACION", "EJECUCION", "FINALIZADO", "INHABILITADO"
    private String estadoEtapas; // "EN TIEMPO", "ATRASADO", "FINALIZADO"
    private String estadoActividades; // "EN TIEMPO", "ATRASADO", "FINALIZADO"

    private LocalDateTime fechaCreacion; // Fecha de creación del proyecto
    private LocalDateTime fechaUltimaActualizacion; // Fecha de la última actualización del proyecto
    private LocalDateTime fechaFinRealEtapas; // Fecha de fin real calculada a partir de las etapas
    private LocalDateTime fechaFinRealActividades; // Fecha de fin real calculada a partir de las actividades
    private LocalDateTime fechaFinRealProyecto; // Fecha de fin real calculada a partir de la fecha de fin real de las etapas y actividades
    
    private String motivoCambio; // Motivo del último cambio (para auditoría)
}
