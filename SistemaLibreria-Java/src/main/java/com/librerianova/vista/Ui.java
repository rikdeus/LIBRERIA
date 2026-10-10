package com.librerianova.vista;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** Utilidades de interfaz compartidas por los paneles. */
final class Ui {

    private Ui() {
    }

    static void error(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    static void info(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    static boolean confirmar(Component padre, String mensaje) {
        return JOptionPane.showConfirmDialog(padre, mensaje, "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    /** Tabla de solo lectura. */
    static JTable tabla(String... columnas) {
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        JTable tabla = new JTable(modelo);
        tabla.setAutoCreateRowSorter(true);
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        return tabla;
    }

    static DefaultTableModel modelo(JTable tabla) {
        return (DefaultTableModel) tabla.getModel();
    }
}
