package com.librerianova.modelo;

/** Productos que admiten un porcentaje de descuento (0 a 50). Una clase puede implementar varias interfaces. */
public interface Descontable {
    int MAX_DESCUENTO = 50;

    int getPorcentajeDescuento();
}
