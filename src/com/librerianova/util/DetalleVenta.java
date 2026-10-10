package com.librerianova.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Rama: feature/calculo-total
 * Línea de venta: libro + cantidad.
 */
public class DetalleVenta {
    private Libro libro;
    private int cantidad;

    public DetalleVenta(Libro libro, int cantidad) {
        if (libro == null) {
            throw new IllegalArgumentException("El detalle debe tener un libro.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        if (cantidad > libro.getStock()) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + libro.getStock());
        }
        this.libro = libro;
        this.cantidad = cantidad;
    }

    public BigDecimal calcularSubtotal() {
        return libro.getPrecio()
                .multiply(BigDecimal.valueOf(cantidad))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return cantidad + " x " + libro.getTitulo() + " = S/ " + calcularSubtotal();
    }
}
