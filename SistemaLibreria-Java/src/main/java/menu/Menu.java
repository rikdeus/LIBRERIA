package menu;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import modelo.Cliente;
import modelo.Pedido;
import modelo.Producto;

public class Menu {

    private Menu() {
    }

    public static void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        Map<Integer, Producto> productos = new HashMap<>();
        List<Cliente> clientes = new ArrayList<>();
        List<Pedido> pedidos = new ArrayList<>();
        productos.put(1, new Producto(1, "Cuaderno Nova A4", 12.50));
        int siguientePedido = 1;
        int opcion;

        do {
            System.out.println("\n================================");
            System.out.println("        LIBRERIA NOVA           ");
            System.out.println("================================");
            System.out.println("1. Registrar producto");
            System.out.println("2. Consultar productos");
            System.out.println("3. Registrar cliente");
            System.out.println("4. Registrar pedido");
            System.out.println("5. Calcular importe total");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opcion: ");

            while (!scanner.hasNextInt()) {
                if (!scanner.hasNextLine()) {
                    return;
                }
                System.out.println("Opcion invalida. Ingrese un numero.");
                scanner.nextLine();
                System.out.print("Seleccione una opcion: ");
            }

            opcion = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (opcion) {
                    case 1:
                        registrarProducto(scanner, productos);
                        break;
                    case 2:
                        consultarProductos(productos);
                        break;
                    case 3:
                        registrarCliente(scanner, clientes);
                        break;
                    case 4:
                        siguientePedido = registrarPedido(scanner, productos, clientes, pedidos, siguientePedido);
                        break;
                    case 5:
                        mostrarTotales(pedidos);
                        break;
                    case 6:
                        System.out.println("\nSaliendo del sistema...");
                        break;
                    default:
                        System.out.println("\nOpcion invalida. Intente de nuevo.");
                }
            } catch (IllegalArgumentException exception) {
                System.out.println("Error: " + exception.getMessage());
            } catch (RuntimeException exception) {
                System.out.println("No se pudo completar la operacion. Intente nuevamente.");
            }
        } while (opcion != 6);

        scanner.close();
    }

    private static void registrarProducto(Scanner scanner, Map<Integer, Producto> productos) {
        System.out.println("\n--- REGISTRAR PRODUCTO ---");
        String nombre = leerTexto(scanner, "Nombre: ");
        double precio = leerDouble(scanner, "Precio: ");
        int id = productos.size() + 1;
        productos.put(id, new Producto(id, nombre, precio));
        System.out.println("Producto registrado correctamente.");
    }

    private static void consultarProductos(Map<Integer, Producto> productos) {
        System.out.println("\n--- PRODUCTOS ---");
        if (productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        for (Map.Entry<Integer, Producto> entrada : productos.entrySet()) {
            System.out.println("Producto " + entrada.getKey() + ":");
            entrada.getValue().mostrarDatos();
        }
    }

    private static void registrarCliente(Scanner scanner, List<Cliente> clientes) {
        System.out.println("\n--- REGISTRAR CLIENTE ---");
        int id = clientes.size() + 1;
        String nombre = leerTexto(scanner, "Nombre: ");
        String correo = leerTexto(scanner, "Correo: ");
        String telefono = leerTexto(scanner, "Telefono: ");
        clientes.add(new Cliente(id, nombre, correo, telefono));
        System.out.println("Cliente registrado correctamente.");
        clientes.get(clientes.size() - 1).mostrarDatos();
    }

    private static int registrarPedido(Scanner scanner, Map<Integer, Producto> productos,
            List<Cliente> clientes, List<Pedido> pedidos, int siguientePedido) {
        if (productos.isEmpty() || clientes.isEmpty()) {
            System.out.println("Debe registrar productos y clientes primero.");
            return siguientePedido;
        }

        consultarClientes(clientes);
        int clienteSeleccionado = leerEntero(scanner, "Numero de cliente: ") - 1;
        consultarProductos(productos);
        int productoSeleccionado = leerEntero(scanner, "ID de producto: ");
        int cantidad = leerEntero(scanner, "Cantidad: ");

        if (clienteSeleccionado < 0 || clienteSeleccionado >= clientes.size()
                || !productos.containsKey(productoSeleccionado)) {
            throw new IllegalArgumentException("cliente o producto no encontrado.");
        }

        pedidos.add(new Pedido(siguientePedido, clientes.get(clienteSeleccionado),
                productos.get(productoSeleccionado), cantidad));
        System.out.println("Pedido registrado correctamente.");
        return siguientePedido + 1;
    }

    private static void consultarClientes(List<Cliente> clientes) {
        System.out.println("\n--- CLIENTES ---");
        for (int i = 0; i < clientes.size(); i++) {
            System.out.println("Cliente " + (i + 1) + ":");
            clientes.get(i).mostrarDatos();
        }
    }

    private static void mostrarTotales(List<Pedido> pedidos) {
        System.out.println("\n--- IMPORTES TOTALES ---");
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }
        for (Pedido pedido : pedidos) {
            pedido.mostrarDatos();
        }
    }

    private static String leerTexto(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        if (!scanner.hasNextLine()) {
            throw new IllegalStateException("no se recibio ningun dato.");
        }
        String valor = scanner.nextLine().trim();
        if (valor.isEmpty()) {
            throw new IllegalArgumentException("el texto no puede estar vacio.");
        }
        return valor;
    }

    private static int leerEntero(Scanner scanner, String mensaje) {
        try {
            return Integer.parseInt(leerTexto(scanner, mensaje));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("debe ingresar un numero entero.");
        }
    }

    private static double leerDouble(Scanner scanner, String mensaje) {
        try {
            double valor = Double.parseDouble(leerTexto(scanner, mensaje));
            if (!Double.isFinite(valor)) {
                throw new IllegalArgumentException("debe ingresar un numero decimal finito.");
            }
            return valor;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("debe ingresar un numero decimal.");
        }
    }
}