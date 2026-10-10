package com.librerianova.util;

import java.math.BigDecimal;

/**
 * Rama: feature/validacion-precios
 * Agrega validaciones al precio del libro.
 */
public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private BigDecimal precio;
    private int stock;

    public Libro(int id, String titulo, String autor, BigDecimal precio, int stock) {
        this.id = id;
        setTitulo(titulo);
        setAutor(autor);
        setPrecio(precio);   // <-- validación
        setStock(stock);
    }

    public BigDecimal getPrecio() { return precio; }

    /**
     * Valida que el precio no sea nulo, negativo ni cero.
     * Usa BigDecimal para evitar errores de redondeo con double.
     */
    public void setPrecio(BigDecimal precio) {
        if (precio == null) {
            throw new IllegalArgumentException("El precio no puede ser nulo.");
        }
        if (precio.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero. Recibido: " + precio);
        }
        // Máximo 2 decimales
        if (precio.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("El precio solo puede tener hasta 2 decimales.");
        }
        this.precio = precio;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        this.titulo = titulo.trim();
    }

    public void setAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío.");
        }
        this.autor = autor.trim();
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getStock() { return stock; }

    @Override
    public String toString() {
        return "Libro{id=" + id + ", titulo='" + titulo + "', autor='" + autor +
               "', precio=" + precio + ", stock=" + stock + "}";
    }
}
