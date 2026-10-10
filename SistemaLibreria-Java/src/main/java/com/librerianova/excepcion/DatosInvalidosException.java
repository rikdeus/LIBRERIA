package com.librerianova.excepcion;

/** Se lanza cuando un dato ingresado no cumple las reglas de validación (RF-31). */
public class DatosInvalidosException extends IllegalArgumentException {
    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
}
