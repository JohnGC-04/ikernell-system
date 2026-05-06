package com.ikernell.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RankingDesarrolladorDTO {
    private String nombreCompleto;
    private double puntajePromedio;
    private long actividadesCompletadas;
}