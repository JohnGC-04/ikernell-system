package com.ikernell.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany (mappedBy = "etapa", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Actividad> actividades;

    public void setEstado(String string) {
        throw new UnsupportedOperationException("Unimplemented method 'setEstado'");
    }

    public String getEstado() {
        // Este método se puede implementar para calcular el estado de la etapa basado en el estado de sus actividades
        throw new UnsupportedOperationException("Unimplemented method 'getEstado'");
    }

    private LocalDateTime fechaFinReal;
}