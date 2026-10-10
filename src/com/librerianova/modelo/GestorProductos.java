package com.librerianova.modelo;

import java.util.ArrayList;

public class GestorProductos {

    private ArrayList<Producto> productos;

    public GestorProductos() {
        productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        productos.add(producto);
    }

    public void listarProductos() {
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        for (Producto producto : productos) {
            producto.mostrarDatos();
            System.out.println("-------------------");
        }
    }

    public int cantidadProductos() {
        return productos.size();
    }
}
