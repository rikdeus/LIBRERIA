# SistemaLibreria-Java — Librería Nova

Aplicación de escritorio en Java para gestionar los **productos, clientes y ventas** de una librería.
Proyecto del curso *Técnicas de Programación Orientada a Objetos* (UPN, 2026-5).

## 🎯 Objetivo
Aplicar programación orientada a objetos, interfaz gráfica y acceso a datos para resolver un problema real,
trabajando de forma colaborativa con Git y GitHub.

## ⚙️ Funcionalidades
- **Productos:** libros y artículos de papelería (alta, edición, baja, búsqueda, stock bajo, descuentos en libros).
- **Clientes:** alta, edición, baja y validación de DNI único.
- **Ventas:** carrito, control de stock, IGV 18 %, registro transaccional, historial y boleta en `.txt`.
- **Archivos:** exportar e importar productos en CSV (con informe de filas con error).
- **Validaciones** y manejo de errores en todos los formularios.

Estado de los requerimientos: ver [`docs/requerimientos.md`](SistemaLibreria-Java/docs/requerimientos.md) (25 de 40 implementados).

## 🧱 Conceptos de POO aplicados
Clases abstractas (`Producto`, `Persona`) · Herencia (`Libro`, `Papeleria`, `Cliente`) · Polimorfismo (`getPrecioFinal`) ·
Interfaces (`Vendible`, `Descontable`, `Repositorio<T>`) · Agregación (`Venta`–`Cliente`) · Composición (`Venta`–`DetalleVenta`) ·
Excepciones propias · Colecciones y streams · Generics.
Diagrama: [`docs/diagrama-clases.md`](SistemaLibreria-Java/docs/diagrama-clases.md).

## 🛠️ Tecnologías
Java 21 · Swing · JDBC + SQL · H2 (base de datos embebida) · Maven · Git y GitHub

## ▶️ Cómo ejecutarlo
Requisitos: JDK 21 y Maven (o abrir `SistemaLibreria-Java` como proyecto Maven en NetBeans).

```bash
cd SistemaLibreria-Java
mvn compile exec:java
```
La base de datos se crea sola en `SistemaLibreria-Java/datos/` la primera vez.

## 📁 Estructura
```
SistemaLibreria-Java/
├── pom.xml
├── docs/                      requerimientos, historias de usuario, UML, guía de Git, etc.
└── src/main/java/com/librerianova/
    ├── Main.java
    ├── modelo/                dominio (POO)
    ├── excepcion/             excepciones propias
    ├── persistencia/          JDBC y CRUD
    ├── servicio/              reportes con colecciones
    ├── util/                  validaciones y archivos
    └── vista/                 interfaz Swing
```

## 🌿 Trabajo colaborativo
Ramas `main` / `develop` / `feature/*`, commits `tipo: mensaje` y Pull Requests con revisión.
Ver [`docs/guia-git.md`](SistemaLibreria-Java/docs/guia-git.md).

## 👥 Integrantes
* LEONARDO ADRIANO CRUZ MORAN
* LUIS ALBERTO LEVANO LIZANA
* KEVIN YEANDER OSCO OSORES
* MARISOL TIRACCAYA ARROYO
* ERICK EDUARDO VERGARA HONORES
* JOSE GUSTAVO ZAPATA DE LA CRUZ

## 📌 Estado del proyecto
En desarrollo. Pendientes principales: historial por cliente, anulación de ventas, login y roles, reportes y gráficos.
