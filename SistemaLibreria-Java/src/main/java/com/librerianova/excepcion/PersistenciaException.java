package com.librerianova.excepcion;

/** Error controlado de la capa de acceso a datos (JDBC / archivos). */
public class PersistenciaException extends Exception {
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    public PersistenciaException(String mensaje) {
        super(mensaje);
    }
}
