package com.ikernell.backend.exception;

// Excepción personalizada para lógica de negocio
public class PresupuestoExcedidoException extends RuntimeException {
    public PresupuestoExcedidoException(String mensaje) {
        super(mensaje);
    }
}