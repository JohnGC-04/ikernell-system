package com.ikernell.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "etapas")
@Data
public class Etapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEtapa;

    @Column(nullable = false)
    private String nombreEtapa; // Ej: Análisis, Diseño, Desarrollo

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_proyecto", nullable = false)
    private Proyecto proyecto;
}