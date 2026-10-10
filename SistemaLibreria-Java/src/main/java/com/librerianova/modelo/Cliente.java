package com.librerianova.modelo;

import com.librerianova.util.Validacion;

/** Cliente de la librería (hereda de Persona y agrega el DNI). */
public class Cliente extends Persona {
    private String dni;

    public Cliente(int id, String dni, String nombre, String correo, String telefono) {
        super(id, nombre, correo, telefono);
        setDni(dni);
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = Validacion.dni(dni);
    }

    @Override
    public String getTipo() {
        return "Cliente";
    }

    @Override
    public String toString() {
        return getNombre() + " (DNI " + dni + ")";
    }
}
