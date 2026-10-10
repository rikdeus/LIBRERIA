package com.librerianova.modelo;

import com.librerianova.util.Validacion;

/** Clase abstracta con los datos de contacto comunes a cualquier persona del sistema. */
public abstract class Persona {
    private int id;
    private String nombre;
    private String correo;
    private String telefono;

    protected Persona(int id, String nombre, String correo, String telefono) {
        this.id = id;
        setNombre(nombre);
        setCorreo(correo);
        setTelefono(telefono);
    }

    public abstract String getTipo();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = Validacion.texto(nombre, "El nombre");
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = Validacion.correo(correo);
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = Validacion.telefono(telefono);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
