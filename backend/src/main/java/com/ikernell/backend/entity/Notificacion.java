package com.ikernell.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;
    private LocalDateTime fechaCreacion;
    private boolean leida = false;
    private String tipo; // Ejemplo: "PRESUPUESTO", "WORKFLOW", "SISTEMA"

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario destinatario;
}