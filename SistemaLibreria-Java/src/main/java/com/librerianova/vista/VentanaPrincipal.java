package com.librerianova.vista;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/** Ventana principal con una pestaña por módulo. */
public class VentanaPrincipal extends JFrame {

    public VentanaPrincipal() {
        super("Librería Nova");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 600));

        PanelProductos productos = new PanelProductos();
        PanelClientes clientes = new PanelClientes();
        PanelVentas ventas = new PanelVentas();

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Productos", productos);
        pestanas.addTab("Clientes", clientes);
        pestanas.addTab("Ventas", ventas);
        // Evento: al cambiar de pestaña se recargan los datos para que siempre estén al día
        pestanas.addChangeListener(e -> {
            switch (pestanas.getSelectedIndex()) {
                case 0 -> productos.refrescar();
                case 1 -> clientes.refrescar();
                case 2 -> ventas.refrescar();
                default -> { }
            }
        });
        add(pestanas);
        pack();
        setLocationRelativeTo(null);
    }
}
