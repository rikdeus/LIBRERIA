package com.librerianova.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Rama: feature/calculo-total
 * Calcula subtotal, IGV (18%) y total de una venta.
 */
public class Venta {
    private static final BigDecimal IGV = new BigDecimal("0.18");
    private static final int MAX_ITEMS = 100;

    private int id;
    private Cliente cliente;
    private final List<DetalleVenta> detalles = new ArrayList<>();

    public Venta(int id, Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("La venta debe tener un cliente.");
        }
        this.id = id;
        this.cliente = cliente;
    }

    public void agregarDetalle(DetalleVenta detalle) {
        if (detalles.size() >= MAX_ITEMS) {
            throw new IllegalStateException("La venta no puede tener más de " + MAX_ITEMS + " ítems.");
        }
        detalles.add(detalle);
    }

    /** Suma de (precio * cantidad) de todos los detalles. */
    public BigDecimal calcularSubtotal() {
        return detalles.stream()
                .map(DetalleVenta::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /** Impuesto: 18% del subtotal. */
    public BigDecimal calcularIGV() {
        return calcularSubtotal().multiply(IGV)
                .setScale(2, RoundingMode.HALF_UP);
    }

    /** Total a pagar = subtotal + IGV. */
    public BigDecimal calcularTotal() {
        return calcularSubtotal().add(calcularIGV())
                .setScale(2, RoundingMode.HALF_UP);
    }

    public void mostrarResumen() {
        System.out.println("===== RESUMEN DE VENTA #" + id + " =====");
        System.out.println("Cliente: " + cliente.getNombre());
        for (DetalleVenta d : detalles) {
            System.out.println("  " + d);
        }
        System.out.println("Subtotal: S/ " + calcularSubtotal());
        System.out.println("IGV (18%): S/ " + calcularIGV());
        System.out.println("TOTAL:    S/ " + calcularTotal());
    }
}
