package com.librerianova.modelo;

import com.librerianova.util.Validacion;
import java.math.BigDecimal;

/** Clase abstracta base de los productos. Cada subclase define su categoría y su precio final. */
public abstract class Producto implements Vendible {
    private int id;
    private String nombre;
    private BigDecimal precio;
    private int stock;

    protected Producto(int id, String nombre, BigDecimal precio, int stock) {
        this.id = id;
        setNombre(nombre);
        setPrecio(precio);
        setStock(stock);
    }

    public abstract String getCategoria();

    /** Texto con los datos propios de cada tipo (autor, marca, etc.). */
    public abstract String getDetalle();

    @Override
    public BigDecimal getPrecioFinal() {
        return precio;
    }

    @Override
    public boolean hayStock(int cantidad) {
        return cantidad > 0 && cantidad <= stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Validacion.texto(nombre, "El nombre");
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = Validacion.precio(precio);
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = Validacion.stock(stock);
    }

    @Override
    public String toString() {
        return nombre + " (" + getCategoria() + ")";
    }
}
