# Requerimientos funcionales — Librería Nova

Estado: ✅ Implementado · 🟡 Parcial · ⬜ Pendiente.
**Resumen: 25 implementados, 1 parcial, 14 pendientes (total 40).**

| ID | Requerimiento | Prior. | Estado | Criterio de aceptación |
|----|---------------|--------|--------|------------------------|
| RF-01 | Registrar libro (título, autor, ISBN, precio, stock, descuento) | Alta | ✅ | Dado un libro válido, al guardar aparece en la tabla con un ID nuevo. |
| RF-02 | Registrar artículo de papelería (nombre, marca, precio, stock) | Alta | ✅ | Dado un artículo válido, al guardar aparece en la tabla con un ID nuevo. |
| RF-03 | Listar productos | Alta | ✅ | Al abrir la pestaña se muestran todos los productos ordenados por nombre. |
| RF-04 | Buscar productos por nombre | Media | ✅ | Al escribir en el buscador, la tabla muestra solo coincidencias (sin distinguir mayúsculas). |
| RF-05 | Editar producto | Alta | ✅ | Al seleccionar una fila y guardar, los cambios persisten tras reiniciar la app. |
| RF-06 | Eliminar producto | Media | ✅ | Se elimina tras confirmar; si tiene ventas, se muestra un error y no se borra. |
| RF-07 | Aplicar descuento (0–50 %) a libros | Media | ✅ | El precio mostrado y el usado en la venta incluyen el descuento. |
| RF-08 | Consultar productos con stock bajo (≤ 5) | Media | ✅ | El botón filtra los productos con stock ≤ 5, de menor a mayor. |
| RF-09 | Ver valor del inventario por categoría | Baja | 🟡 | La lógica existe (`Reportes`), falta mostrarla en pantalla. |
| RF-10 | Ordenar la tabla de productos por cualquier columna | Baja | ✅ | Al hacer clic en el encabezado, la tabla se reordena. |
| RF-11 | Registrar cliente (DNI, nombre, correo, teléfono) | Alta | ✅ | Con datos válidos el cliente queda guardado. |
| RF-12 | Editar cliente | Media | ✅ | Los cambios persisten tras guardar. |
| RF-13 | Eliminar cliente | Baja | ✅ | Se elimina tras confirmar; si tiene ventas, no se permite. |
| RF-14 | Listar clientes | Alta | ✅ | Se muestran todos los clientes registrados. |
| RF-15 | Impedir DNI duplicado | Alta | ✅ | Registrar un DNI existente muestra un error y no guarda. |
| RF-16 | Ver historial de compras de un cliente | Media | ⬜ | Al elegir un cliente se listan solo sus ventas. |
| RF-17 | Seleccionar cliente para una venta | Alta | ✅ | La venta queda asociada al cliente elegido. |
| RF-18 | Agregar productos al carrito controlando stock | Alta | ✅ | Si la cantidad supera el stock, se rechaza con el stock disponible. |
| RF-19 | Quitar ítems del carrito | Media | ✅ | El ítem desaparece y los totales se recalculan. |
| RF-20 | Calcular subtotal, IGV (18 %) y total | Alta | ✅ | Para 3 × S/ 40.00 + 2 × S/ 12.50 el total es S/ 171.10. |
| RF-21 | Registrar la venta y descontar stock | Alta | ✅ | Se guarda todo o nada (transacción); el stock baja según lo vendido. |
| RF-22 | Ver historial de ventas | Media | ✅ | Se listan N°, fecha, cliente y total, de la más reciente a la más antigua. |
| RF-23 | Guardar la boleta en un archivo de texto | Media | ✅ | Se genera un `.txt` con detalle, subtotal, IGV y total. |
| RF-24 | Ver el detalle de una venta del historial | Media | ⬜ | Al elegir una venta se muestran sus ítems. |
| RF-25 | Anular una venta y devolver el stock | Media | ⬜ | La venta queda anulada y el stock se restituye. |
| RF-26 | Descuento para clientes frecuentes | Baja | ⬜ | Desde N compras, se aplica el % configurado. |
| RF-27 | Exportar productos a CSV | Media | ✅ | Se genera un CSV que se puede abrir en Excel. |
| RF-28 | Importar productos desde CSV | Media | ✅ | Se importan filas válidas y se informa de las filas con error. |
| RF-29 | Respaldar la base de datos | Baja | ⬜ | Se genera una copia de `datos/` con fecha en el nombre. |
| RF-30 | Exportar historial de ventas a CSV | Baja | ⬜ | Se genera un CSV con todas las ventas. |
| RF-31 | Validar los datos ingresados | Alta | ✅ | Datos vacíos, negativos o con formato inválido se rechazan con un mensaje claro. |
| RF-32 | Manejar errores sin cerrar el programa | Alta | ✅ | Ante un error de BD o archivo se muestra un mensaje y la app sigue funcionando. |
| RF-33 | Inicio de sesión de usuarios | Media | ⬜ | Solo usuarios registrados acceden al sistema. |
| RF-34 | Roles (administrador / vendedor) | Media | ⬜ | El vendedor no puede eliminar productos ni clientes. |
| RF-35 | Registro de auditoría de acciones | Baja | ⬜ | Cada alta/baja/venta queda registrada con usuario y hora. |
| RF-36 | Reporte de ventas por día | Media | ⬜ | Se muestra el total vendido por fecha. |
| RF-37 | Reporte de productos más vendidos | Media | ⬜ | Se listan los 10 productos con más unidades vendidas. |
| RF-38 | Gráfico de ventas (componentes gráficos) | Baja | ⬜ | Se muestra un gráfico de barras de ventas por mes. |
| RF-39 | Reporte de inventario por categoría en pantalla | Baja | ⬜ | Ver RF-09. |
| RF-40 | Sonido de confirmación al registrar una venta | Baja | ⬜ | Se reproduce un audio corto al confirmar la venta. |

> Cuenten con que el docente pide **40 requerimientos documentados** y **20 implementados** (ficha de práctica de campo).
> Actualicen la columna *Estado* conforme avancen (semana 7: "Actualización estado de requerimientos y criterios de aceptación").
