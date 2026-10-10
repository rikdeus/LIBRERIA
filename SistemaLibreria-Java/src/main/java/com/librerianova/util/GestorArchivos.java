package com.librerianova.util;

import com.librerianova.excepcion.DatosInvalidosException;
import com.librerianova.excepcion.PersistenciaException;
import com.librerianova.modelo.DetalleVenta;
import com.librerianova.modelo.Libro;
import com.librerianova.modelo.Papeleria;
import com.librerianova.modelo.Producto;
import com.librerianova.modelo.Venta;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Manejo de archivos: exportar/importar productos en CSV y guardar la boleta de una venta en texto.
 * Formato CSV: tipo,nombre,precio,stock,autor_o_marca,isbn,descuento
 */
public final class GestorArchivos {

    private static final String CABECERA = "tipo,nombre,precio,stock,autor_o_marca,isbn,descuento";

    private GestorArchivos() {
    }

    public static void exportarProductos(List<Producto> productos, Path destino) throws PersistenciaException {
        try (BufferedWriter w = Files.newBufferedWriter(destino, StandardCharsets.UTF_8)) {
            w.write(CABECERA);
            w.newLine();
            for (Producto p : productos) {
                String extra = "";
                String isbn = "";
                int descuento = 0;
                if (p instanceof Libro l) {
                    extra = l.getAutor();
                    isbn = l.getIsbn();
                    descuento = l.getPorcentajeDescuento();
                } else if (p instanceof Papeleria pa) {
                    extra = pa.getMarca();
                }
                w.write(String.join(",", campo(p.getCategoria()), campo(p.getNombre()),
                        p.getPrecio().toPlainString(), String.valueOf(p.getStock()),
                        campo(extra), campo(isbn), String.valueOf(descuento)));
                w.newLine();
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo escribir el archivo: " + e.getMessage(), e);
        }
    }

    /**
     * Lee un CSV de productos. Las filas inválidas no detienen la importación:
     * se agregan al informe de errores (parámetro) y se continúa con las demás.
     */
    public static List<Producto> importarProductos(Path origen, List<String> errores) throws PersistenciaException {
        List<Producto> productos = new ArrayList<>();
        List<String> lineas;
        try {
            lineas = Files.readAllLines(origen, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo leer el archivo: " + e.getMessage(), e);
        }
        for (int i = 1; i < lineas.size(); i++) { // la línea 0 es la cabecera
            if (lineas.get(i).isBlank()) {
                continue;
            }
            try {
                productos.add(convertirFila(partir(lineas.get(i))));
            } catch (DatosInvalidosException | ArrayIndexOutOfBoundsException e) {
                errores.add("Línea " + (i + 1) + ": " + (e instanceof DatosInvalidosException
                        ? e.getMessage() : "faltan columnas."));
            }
        }
        return productos;
    }

    private static Producto convertirFila(String[] c) {
        String tipo = c[0].trim();
        BigDecimal precio = Validacion.parsearPrecio(c[2]);
        int stock = Validacion.parsearEntero(c[3], "El stock");
        if ("Libro".equalsIgnoreCase(tipo)) {
            int desc = c.length > 6 && !c[6].isBlank() ? Validacion.parsearEntero(c[6], "El descuento") : 0;
            return new Libro(0, c[1], precio, stock, c[4], c.length > 5 ? c[5] : "", desc);
        }
        if (tipo.toLowerCase().startsWith("papeler")) {
            return new Papeleria(0, c[1], precio, stock, c[4]);
        }
        throw new DatosInvalidosException("Tipo desconocido \"" + tipo + "\" (use Libro o Papelería).");
    }

    public static void guardarBoleta(Venta venta, Path destino) throws PersistenciaException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        try (BufferedWriter w = Files.newBufferedWriter(destino, StandardCharsets.UTF_8)) {
            w.write("LIBRERIA NOVA - BOLETA DE VENTA #" + venta.getId());
            w.newLine();
            w.write("Fecha:   " + venta.getFecha().format(fmt));
            w.newLine();
            w.write("Cliente: " + venta.getCliente().getNombre() + " (DNI " + venta.getCliente().getDni() + ")");
            w.newLine();
            w.write("----------------------------------------");
            w.newLine();
            for (DetalleVenta d : venta.getDetalles()) {
                w.write(d.toString());
                w.newLine();
            }
            w.write("----------------------------------------");
            w.newLine();
            w.write("Subtotal:  S/ " + venta.calcularSubtotal());
            w.newLine();
            w.write("IGV (18%): S/ " + venta.calcularIGV());
            w.newLine();
            w.write("TOTAL:     S/ " + venta.calcularTotal());
            w.newLine();
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo guardar la boleta: " + e.getMessage(), e);
        }
    }

    /** Encierra el valor entre comillas si contiene coma o comillas. */
    private static String campo(String valor) {
        if (valor.contains(",") || valor.contains("\"")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }

    /** Divide una línea CSV respetando los campos entre comillas. */
    static String[] partir(String linea) {
        List<String> campos = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean entreComillas = false;
        for (int i = 0; i < linea.length(); i++) {
            char ch = linea.charAt(i);
            if (ch == '"') {
                if (entreComillas && i + 1 < linea.length() && linea.charAt(i + 1) == '"') {
                    actual.append('"');
                    i++;
                } else {
                    entreComillas = !entreComillas;
                }
            } else if (ch == ',' && !entreComillas) {
                campos.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(ch);
            }
        }
        campos.add(actual.toString());
        return campos.toArray(new String[0]);
    }
}
