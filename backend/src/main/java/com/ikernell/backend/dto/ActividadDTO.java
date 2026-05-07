package com.ikernell.backend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ActividadDTO {

    private Long idActividad;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(min = 10, max = 255, message = "La descripción debe tener entre 10 y 255 caracteres")
    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "Pendiente|En Progreso|Terminado", message = "Estado no válido")
    private String estado;

    @NotNull(message = "El costo es obligatorio")
    @Positive(message = "El costo debe ser mayor a cero")
    private Double costoEstimado;

    @NotNull(message = "La fecha de entrega planeada es obligatoria")
    @Future(message = "La fecha de entrega debe ser una fecha futura")
    private LocalDateTime fechaEntregaPlaneada;

    @NotNull(message = "Debe asignar una etapa")
    private Long idEtapa;

    @NotNull(message = "Debe asignar un desarrollador")
    private Long idDesarrollador;

    private Long idProyecto; // Necesario para la validación de presupuesto
    
    // Campos de solo lectura para respuestas
    private String nombreEtapa;
    private String nombreDesarrollador;
}