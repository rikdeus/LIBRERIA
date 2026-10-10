package com.librerianova.persistencia;

import com.librerianova.excepcion.PersistenciaException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Abre conexiones JDBC a la base H2 embebida (archivo en ./datos) y crea las tablas si no existen. */
public final class Conexion {

    private static final String URL_POR_DEFECTO = "jdbc:h2:./datos/libreria";
    private static boolean inicializada = false;

    private Conexion() {
    }

    private static String url() {
        return System.getProperty("libreria.db.url", URL_POR_DEFECTO);
    }

    public static synchronized Connection obtener() throws PersistenciaException {
        try {
            Connection conexion = DriverManager.getConnection(url(), "sa", "");
            if (!inicializada) {
                crearTablas(conexion);
                inicializada = true;
            }
            return conexion;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo conectar a la base de datos.", e);
        }
    }

    private static void crearTablas(Connection conexion) throws SQLException {
        try (Statement st = conexion.createStatement()) {
            st.execute("""
                CREATE TABLE IF NOT EXISTS producto (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    tipo VARCHAR(12) NOT NULL,
                    nombre VARCHAR(120) NOT NULL,
                    precio DECIMAL(10,2) NOT NULL,
                    stock INT NOT NULL,
                    autor VARCHAR(120),
                    isbn VARCHAR(30),
                    descuento INT DEFAULT 0,
                    marca VARCHAR(80)
                )""");
            st.execute("""
                CREATE TABLE IF NOT EXISTS cliente (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    dni VARCHAR(8) NOT NULL UNIQUE,
                    nombre VARCHAR(120) NOT NULL,
                    correo VARCHAR(120) NOT NULL,
                    telefono VARCHAR(9) NOT NULL
                )""");
            st.execute("""
                CREATE TABLE IF NOT EXISTS venta (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    cliente_id INT NOT NULL REFERENCES cliente(id),
                    fecha TIMESTAMP NOT NULL,
                    subtotal DECIMAL(10,2) NOT NULL,
                    igv DECIMAL(10,2) NOT NULL,
                    total DECIMAL(10,2) NOT NULL
                )""");
            st.execute("""
                CREATE TABLE IF NOT EXISTS detalle_venta (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    venta_id INT NOT NULL REFERENCES venta(id) ON DELETE CASCADE,
                    producto_id INT NOT NULL REFERENCES producto(id),
                    cantidad INT NOT NULL,
                    precio_unitario DECIMAL(10,2) NOT NULL
                )""");
        }
    }
}
