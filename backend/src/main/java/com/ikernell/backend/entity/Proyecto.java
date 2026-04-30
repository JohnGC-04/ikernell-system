package com.ikernell.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "proyectos")
@Data
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProyecto;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    @Column(nullable = false)
    private String estado; // "PLANIFICACION", "EJECUCION", "FINALIZADO", "INHABILITADO"

    // Relación Many-to-One: Muchos proyectos pueden tener el mismo Líder
    @ManyToOne
    @JoinColumn(name = "id_lider", nullable = false)
    private Usuario lider;
}