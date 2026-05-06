package com.ikernell.backend.dto;

import lombok.Data;

@Data
public class AlertaPresupuestoDTO {
    private String mensaje;
    private double porcentajeConsumido;
    private double saldoRestante;
    private boolean riesgoAlto;
}