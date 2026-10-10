package com.librerianova.modelo;

import com.librerianova.excepcion.DatosInvalidosException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Venta de la librería.
 * - AGREGACIÓN con Cliente: el cliente existe por sí mismo, la venta solo lo referencia.
 * - COMPOSICIÓN con DetalleVenta: los detalles nacen y mueren con la venta.
 */
public class Venta {
    public static final BigDecimal IGV = new BigDecimal("0.18");
    public static final int MAX_ITEMS = 100;

    private int id;
    private final Cliente cliente;
    private final LocalDateTime fecha;
    private final List<DetalleVenta> detalles = new ArrayList<>();

    public Venta(Cliente cliente) {
        if (cliente == null) {
            throw new DatosInvalidosException("La venta debe tener un cliente.");
        }
        this.cliente = cliente;
        this.fecha = LocalDateTime.now();
    }

    /** Agrega un producto; si ya estaba en la venta, suma las cantidades. */
    public void agregarItem(Producto producto, int cantidad) {
        int total = cantidad;
        for (int i = 0; i < detalles.size(); i++) {
            if (detalles.get(i).getProducto().getId() == producto.getId()) {
                total += detalles.get(i).getCantidad();
                DetalleVenta nuevo = new DetalleVenta(producto, total); // valida stock con el total
                detalles.set(i, nuevo);
                return;
            }
        }
        if (detalles.size() >= MAX_ITEMS) {
            throw new IllegalStateException("La venta no puede tener más de " + MAX_ITEMS + " ítems.");
        }
        detalles.add(new DetalleVenta(producto, total));
    }

    public void quitarItem(int indice) {
        if (indice < 0 || indice >= detalles.size()) {
            throw new DatosInvalidosException("Seleccione un ítem válido.");
        }
        detalles.remove(indice);
    }

    public BigDecimal calcularSubtotal() {
        return detalles.stream()
                .map(DetalleVenta::calcularSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularIGV() {
        return calcularSubtotal().multiply(IGV).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularTotal() {
        return calcularSubtotal().add(calcularIGV());
    }

    public boolean estaVacia() {
        return detalles.isEmpty();
    }

    public List<DetalleVenta> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
