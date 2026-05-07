package com.ikernell.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_presupuesto")
@Data
public class AuditoriaPresupuesto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double montoAnterior;
    private Double montoNuevo;
    private LocalDateTime fechaCambio;
    private String motivo;
    
    @Column(name = "usuario_responsable")
    private String usuarioResponsable; // Guardaremos el email del Lider/Coordinador

    @ManyToOne
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;
}