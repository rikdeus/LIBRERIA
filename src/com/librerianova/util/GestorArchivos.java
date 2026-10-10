package com.librerianova.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class GestorArchivos {

    private static final Path CARPETA_DATOS = Paths.get("datos");
    private static final Path ARCHIVO_PRODUCTOS =
            CARPETA_DATOS.resolve("productos.csv");

    public static void guardarProducto(String nombre, double precio) {

        try {
            Files.createDirectories(CARPETA_DATOS);

            boolean archivoNuevo =
                    !Files.exists(ARCHIVO_PRODUCTOS);

            try (BufferedWriter writer = Files.newBufferedWriter(
                    ARCHIVO_PRODUCTOS,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {

                if (archivoNuevo) {
                    writer.write("nombre,precio");
                    writer.newLine();
                }

                writer.write(nombre + "," + precio);
                writer.newLine();
            }

            System.out.println(
                    "Producto guardado correctamente en productos.csv"
            );

        } catch (IOException e) {

            System.out.println(
                    "Error al guardar el producto: "
                    + e.getMessage()
            );
        }
    }
}
