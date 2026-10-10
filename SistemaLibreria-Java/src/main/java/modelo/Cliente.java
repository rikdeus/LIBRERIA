package modelo;

public class Cliente {
    private int id;
    private String nombre;
    private String correo;
    private String telefono;

    public Cliente(int id, String nombre, String correo, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void mostrarDatos() {
        System.out.println("ID: " + id + " | Nombre: " + nombre + " | Correo: " + correo + " | Teléfono: " + telefono);
    }
}