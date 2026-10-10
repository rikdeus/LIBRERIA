package com.librerianova.modelo;

import com.librerianova.util.Validacion;
import java.math.BigDecimal;

/** Artículo de papelería (cuadernos, lapiceros, etc.). No tiene descuento. */
public class Papeleria extends Producto {
    private String marca;

    public Papeleria(int id, String nombre, BigDecimal precio, int stock, String marca) {
        super(id, nombre, precio, stock);
        setMarca(marca);
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = Validacion.texto(marca, "La marca");
    }

    @Override
    public String getCategoria() {
        return "Papelería";
    }

    @Override
    public String getDetalle() {
        return "Marca " + marca;
    }
}
