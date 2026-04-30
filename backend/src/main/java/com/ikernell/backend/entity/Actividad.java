package com.ikernell.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "actividades")
@Data
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idActividad;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private String estado; // "PENDIENTE", "EN_PROCESO", "COMPLETADA"

    private Double costoEstimado;

    @ManyToOne
    @JoinColumn(name = "id_etapa", nullable = false)
    private Etapa etapa;

    @ManyToOne
    @JoinColumn(name = "id_desarrollador", nullable = false)
    private Usuario desarrollador;
}