# Diagrama de clases (UML)

Se visualiza directamente en GitHub (Mermaid). Cubre herencia, clases abstractas, interfaces,
polimorfismo, agregación y composición.

```mermaid
classDiagram
    direction TB

    class Vendible {
        <<interface>>
        +getPrecioFinal() BigDecimal
        +hayStock(int) boolean
    }
    class Descontable {
        <<interface>>
        +getPorcentajeDescuento() int
    }
    class Producto {
        <<abstract>>
        -int id
        -String nombre
        -BigDecimal precio
        -int stock
        +getCategoria()* String
        +getDetalle()* String
        +getPrecioFinal() BigDecimal
    }
    class Libro {
        -String autor
        -String isbn
        -int porcentajeDescuento
        +getPrecioFinal() BigDecimal
    }
    class Papeleria {
        -String marca
    }
    class Persona {
        <<abstract>>
        -int id
        -String nombre
        -String correo
        -String telefono
        +getTipo()* String
    }
    class Cliente {
        -String dni
    }
    class Venta {
        -int id
        -LocalDateTime fecha
        +agregarItem(Producto, int)
        +quitarItem(int)
        +calcularSubtotal() BigDecimal
        +calcularIGV() BigDecimal
        +calcularTotal() BigDecimal
    }
    class DetalleVenta {
        -int cantidad
        -BigDecimal precioUnitario
        +calcularSubtotal() BigDecimal
    }

    Vendible <|.. Producto
    Descontable <|.. Libro
    Producto <|-- Libro
    Producto <|-- Papeleria
    Persona <|-- Cliente
    Cliente "1" o-- "0..*" Venta : agregación
    Venta "1" *-- "1..*" DetalleVenta : composición
    DetalleVenta "*" --> "1" Producto : referencia

    class Repositorio~T~ {
        <<interface>>
        +guardar(T) T
        +buscarPorId(int) Optional~T~
        +listar() List~T~
        +actualizar(T)
        +eliminar(int)
    }
    class ProductoDAO
    class ClienteDAO
    class VentaDAO
    Repositorio <|.. ProductoDAO
    Repositorio <|.. ClienteDAO
    ProductoDAO ..> Producto
    ClienteDAO ..> Cliente
    VentaDAO ..> Venta
```

## Capas del proyecto

| Paquete | Responsabilidad |
|---------|-----------------|
| `modelo` | Clases del dominio y sus reglas de negocio. |
| `excepcion` | Excepciones propias (datos inválidos, stock, persistencia). |
| `persistencia` | JDBC: conexión, CRUD y transacciones sobre H2. |
| `servicio` | Consultas con colecciones (`Reportes`). |
| `util` | Validaciones y manejo de archivos (CSV, boletas). |
| `vista` | Interfaz gráfica Swing (un panel por módulo). |
