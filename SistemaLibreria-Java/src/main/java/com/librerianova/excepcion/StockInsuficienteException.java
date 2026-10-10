package com.librerianova.excepcion;

/** Se lanza cuando se pide más unidades de las que hay disponibles (RF-18). */
public class StockInsuficienteException extends RuntimeException {
    private final int disponible;

    public StockInsuficienteException(String producto, int disponible) {
        super("Stock insuficiente para \"" + producto + "\". Disponible: " + disponible);
        this.disponible = disponible;
    }

    public int getDisponible() {
        return disponible;
    }
}
