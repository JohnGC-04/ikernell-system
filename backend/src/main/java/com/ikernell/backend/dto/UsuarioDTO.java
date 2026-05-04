package com.ikernell.backend.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UsuarioDTO {
    private Long idUsuario;
    private String identificacion;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private String estado;
    private String direccion;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String foto;
    private String perfilProfesional;
    private String especialidad;
}