package com.ikernell.backend.dto;

import lombok.Data;

@Data
public class EtapaDTO {
    private Long idEtapa;
    private String nombreEtapa;
    private String descripcion;
    private Long idProyecto;
    private String nombreProyecto;
}