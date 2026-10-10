package com.librerianova.modelo;

import com.librerianova.excepcion.DatosInvalidosException;
import com.librerianova.excepcion.StockInsuficienteException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Línea de una venta. Es parte de Venta (COMPOSICIÓN): su constructor es de paquete,
 * por eso solo una Venta puede crearla y no existe sin ella.
 * Guarda el precio unitario del momento de la venta.
 */
public class DetalleVenta {
    private final Producto producto;
    private final int cantidad;
    private final BigDecimal precioUnitario;

    DetalleVenta(Producto producto, int cantidad) {
        if (producto == null) {
            throw new DatosInvalidosException("El detalle debe tener un producto.");
        }
        if (cantidad <= 0) {
            throw new DatosInvalidosException("La cantidad debe ser mayor que cero.");
        }
        if (!producto.hayStock(cantidad)) {
            throw new StockInsuficienteException(producto.getNombre(), producto.getStock());
        }
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecioFinal();
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public BigDecimal calcularSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad)).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return cantidad + " x " + producto.getNombre() + " @ S/ " + precioUnitario + " = S/ " + calcularSubtotal();
    }
}
