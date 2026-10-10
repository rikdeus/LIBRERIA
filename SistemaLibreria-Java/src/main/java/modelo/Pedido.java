package modelo;

public class Pedido {

    private int id;
    private Cliente cliente;
    private Producto producto;
    private int cantidad;

    public Pedido(int id, Cliente cliente, Producto producto) {
        this(id, cliente, producto, 1);
    }

    public Pedido(int id, Cliente cliente, Producto producto, int cantidad) {
        if (id <= 0) {
            throw new IllegalArgumentException("el ID debe ser mayor que cero.");
        }
        if (cliente == null || producto == null) {
            throw new IllegalArgumentException("el pedido necesita cliente y producto.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("la cantidad debe ser mayor que cero.");
        }
        this.id = id;
        this.cliente = cliente;
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double calcularSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    public double calcularSubtotal(int cantidadSolicitada) {
        if (cantidadSolicitada <= 0) {
            throw new IllegalArgumentException("la cantidad debe ser mayor que cero.");
        }
        return producto.getPrecio() * cantidadSolicitada;
    }

    public void mostrarDatos() {
        System.out.println("ID Pedido: " + id);
        System.out.println("Cliente: " + cliente.getNombre());
        System.out.println("Producto: " + producto.getNombre());
        System.out.println("Precio unitario: S/ " + producto.getPrecio());
        System.out.println("Cantidad: " + cantidad);
        System.out.println("Importe total: S/ " + calcularSubtotal());
    }
}