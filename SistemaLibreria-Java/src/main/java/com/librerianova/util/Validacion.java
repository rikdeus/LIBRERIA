package com.librerianova.util;

import com.librerianova.excepcion.DatosInvalidosException;
import java.math.BigDecimal;
import java.util.regex.Pattern;

/** Validaciones reutilizables. Todas lanzan DatosInvalidosException con un mensaje claro. */
public final class Validacion {

    private static final Pattern CORREO = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern DNI = Pattern.compile("^\\d{8}$");
    private static final Pattern TELEFONO = Pattern.compile("^\\d{9}$");

    private Validacion() {
    }

    public static String texto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatosInvalidosException(campo + " no puede estar vacío.");
        }
        return valor.trim();
    }

    public static BigDecimal precio(BigDecimal valor) {
        if (valor == null) {
            throw new DatosInvalidosException("El precio no puede ser nulo.");
        }
        if (valor.signum() <= 0) {
            throw new DatosInvalidosException("El precio debe ser mayor que cero.");
        }
        if (valor.stripTrailingZeros().scale() > 2) {
            throw new DatosInvalidosException("El precio admite hasta 2 decimales.");
        }
        return valor;
    }

    public static BigDecimal parsearPrecio(String texto) {
        try {
            return precio(new BigDecimal(texto(texto, "El precio").replace(',', '.')));
        } catch (NumberFormatException e) {
            throw new DatosInvalidosException("El precio debe ser un número (ej. 12.50).");
        }
    }

    public static int parsearEntero(String texto, String campo) {
        try {
            return Integer.parseInt(texto(texto, campo));
        } catch (NumberFormatException e) {
            throw new DatosInvalidosException(campo + " debe ser un número entero.");
        }
    }

    public static int stock(int valor) {
        if (valor < 0) {
            throw new DatosInvalidosException("El stock no puede ser negativo.");
        }
        return valor;
    }

    public static String dni(String valor) {
        String v = texto(valor, "El DNI");
        if (!DNI.matcher(v).matches()) {
            throw new DatosInvalidosException("El DNI debe tener 8 dígitos.");
        }
        return v;
    }

    public static String correo(String valor) {
        String v = texto(valor, "El correo");
        if (!CORREO.matcher(v).matches()) {
            throw new DatosInvalidosException("El correo no tiene un formato válido.");
        }
        return v;
    }

    public static String telefono(String valor) {
        String v = texto(valor, "El teléfono");
        if (!TELEFONO.matcher(v).matches()) {
            throw new DatosInvalidosException("El teléfono debe tener 9 dígitos.");
        }
        return v;
    }
}
