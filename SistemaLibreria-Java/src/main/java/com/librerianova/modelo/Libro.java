package com.librerianova.modelo;

import com.librerianova.excepcion.DatosInvalidosException;
import com.librerianova.util.Validacion;
import java.math.BigDecimal;
import java.math.RoundingMode;

/** Libro: hereda de Producto y además admite descuento (interfaz Descontable). */
public class Libro extends Producto implements Descontable {
    private String autor;
    private String isbn;
    private int porcentajeDescuento;

    public Libro(int id, String nombre, BigDecimal precio, int stock,
                 String autor, String isbn, int porcentajeDescuento) {
        super(id, nombre, precio, stock);
        setAutor(autor);
        setIsbn(isbn);
        setPorcentajeDescuento(porcentajeDescuento);
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = Validacion.texto(autor, "El autor");
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn == null ? "" : isbn.trim();
    }

    @Override
    public int getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(int porcentaje) {
        if (porcentaje < 0 || porcentaje > MAX_DESCUENTO) {
            throw new DatosInvalidosException("El descuento debe estar entre 0 y " + MAX_DESCUENTO + "%.");
        }
        this.porcentajeDescuento = porcentaje;
    }

    /** Polimorfismo: el precio final de un libro aplica su descuento. */
    @Override
    public BigDecimal getPrecioFinal() {
        BigDecimal factor = BigDecimal.valueOf(100 - porcentajeDescuento)
                .divide(BigDecimal.valueOf(100));
        return getPrecio().multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getCategoria() {
        return "Libro";
    }

    @Override
    public String getDetalle() {
        return autor + (isbn.isEmpty() ? "" : " | ISBN " + isbn)
                + (porcentajeDescuento > 0 ? " | -" + porcentajeDescuento + "%" : "");
    }
}
