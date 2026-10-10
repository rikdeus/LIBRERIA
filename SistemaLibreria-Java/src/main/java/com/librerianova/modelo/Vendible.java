package com.librerianova.modelo;

import java.math.BigDecimal;

/** Contrato de todo lo que puede venderse en la librería. */
public interface Vendible {
    BigDecimal getPrecioFinal();

    boolean hayStock(int cantidad);
}
