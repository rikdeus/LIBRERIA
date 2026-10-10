# Problema, restricciones, objetivos y alcance

> Completar y ajustar con la realidad de la librería que modelan. Lo que sigue es una base para que el equipo la edite.

## Problema identificado
La gestión manual de inventario, clientes y ventas provoca errores de stock, cálculos incorrectos de totales y falta de historial.

## Diagrama de Ishikawa (causa – efecto)

**Efecto: errores y demoras en la gestión de la librería**

| Categoría | Causas |
|-----------|--------|
| Método | Registros en cuaderno u hojas sueltas · Sin control de stock en tiempo real |
| Personas | Cálculo manual del IGV · Falta de capacitación en herramientas digitales |
| Materiales | Datos de clientes dispersos · Precios desactualizados |
| Medición | Sin reportes de ventas ni de productos más vendidos |
| Entorno | Picos de demanda (inicio de clases) · Pérdida de información por falta de respaldo |

## Restricciones realistas
- **Técnicas:** aplicación de escritorio en Java 21; base de datos embebida (sin servidor).
- **Tiempo:** 8 semanas de curso.
- **Económicas:** solo herramientas gratuitas (NetBeans, H2, GitHub).
- **Humanas:** equipo de 6 estudiantes con disponibilidad parcial.

## Alternativas de solución
| Alternativa | Ventaja | Desventaja | Decisión |
|-------------|---------|------------|----------|
| Hoja de cálculo | Rápida | Sin control de stock ni validaciones | Descartada |
| Aplicación de consola | Simple | Poco usable | Descartada (versión inicial) |
| **Aplicación de escritorio con BD** | Usable y persistente | Más trabajo | **Elegida** |
| Aplicación web | Acceso remoto | Fuera del alcance del curso | Descartada |

## Objetivo general
Desarrollar una aplicación de escritorio en Java que permita gestionar productos, clientes y ventas de una librería.

## Objetivos específicos
1. Aplicar el paradigma orientado a objetos (herencia, polimorfismo, interfaces, composición).
2. Persistir la información en una base de datos mediante JDBC.
3. Ofrecer una interfaz gráfica con Swing.
4. Trabajar de forma colaborativa con Git y GitHub.

## Alcance
**Incluye:** productos, clientes, ventas con IGV, boletas, importación/exportación CSV.
**No incluye (por ahora):** facturación electrónica SUNAT, venta en línea, múltiples sucursales.

## Antecedentes y referencias (formato APA 7)
Agreguen aquí **antecedentes reales** que ustedes hayan consultado (tesis, artículos, sistemas similares) con su cita.
Referencias del sílabo que pueden usar para la parte técnica:

- Blasco, F. (2020). *Programación Java: JDBC y Swing*. Biblioteca Virtual UPN.
- Jiménez de Parga, C. (2021). *UML: arquitectura de aplicaciones en Java, C++ y Python*. Biblioteca Virtual UPN.
- Vegas Gertrudix, J. M. (2021). *Java 17: fundamentos prácticos de programación*. Biblioteca Virtual UPN.
