package com.librerianova.servicio;

import com.librerianova.modelo.Producto;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Consultas sobre colecciones (List, Map, Comparator y streams). */
public final class Reportes {

    private Reportes() {
    }

    public static List<Producto> buscarPorNombre(List<Producto> productos, String texto) {
        String t = texto == null ? "" : texto.trim().toLowerCase();
        return productos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(t))
                .collect(Collectors.toList());
    }

    public static List<Producto> ordenarPorPrecio(List<Producto> productos) {
        return productos.stream()
                .sorted(Comparator.comparing(Producto::getPrecioFinal))
                .collect(Collectors.toList());
    }

    public static List<Producto> stockBajo(List<Producto> productos, int minimo) {
        return productos.stream()
                .filter(p -> p.getStock() <= minimo)
                .sorted(Comparator.comparingInt(Producto::getStock))
                .collect(Collectors.toList());
    }

    /** Valor del inventario (precio final x stock) agrupado por categoría. */
    public static Map<String, BigDecimal> valorInventarioPorCategoria(List<Producto> productos) {
        Map<String, BigDecimal> resultado = new TreeMap<>();
        for (Producto p : productos) {
            BigDecimal valor = p.getPrecioFinal().multiply(BigDecimal.valueOf(p.getStock()));
            resultado.merge(p.getCategoria(), valor, BigDecimal::add);
        }
        return resultado;
    }
}
