package com.librerianova.util;

public class Validacion {

    public static boolean textoValido(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    public static boolean numeroPositivo(double numero) {
        return numero >= 0;
    }

    public static boolean opcionMenuValida(int opcion) {
        return opcion >= 1 && opcion <= 4;
    }
}
