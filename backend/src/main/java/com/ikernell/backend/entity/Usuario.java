package com.ikernell.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    // Información de Acceso (RF-002, RNF-001)
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // Se guardará con BCrypt (RNF-003)

    @Enumerated(EnumType.STRING) // Guarda el nombre del enum ("LIDER") como texto en la DB
    @Column(nullable = false)
    private RolUsuario rol;

    @Column(nullable = false)
    private String estado; // "ACTIVO", "INACTIVO"

    // Información Personal (RF-047)
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(unique = true, nullable = false)
    private String identificacion;

    private String direccion;

    private String telefono;

    private LocalDate fechaNacimiento;

    // Gestión de Perfil y Multimedia
    private String foto; // Ruta o URL de la imagen

    // Información Profesional
    private String perfilProfesional; // Descripción breve

    private String especialidad; // Ej: Backend, Frontend, QA
}