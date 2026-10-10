package modelo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GestorArchivos {

    public static void leerDatosArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            System.out.println("=== Leyendo datos del archivo ===");
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.err.println("Error controlado: No se pudo leer el archivo. Detalles: " + e.getMessage());
        }
    }
}