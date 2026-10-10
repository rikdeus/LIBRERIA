package com.librerianova.vista;

import com.librerianova.excepcion.DatosInvalidosException;
import com.librerianova.excepcion.PersistenciaException;
import com.librerianova.excepcion.StockInsuficienteException;
import com.librerianova.modelo.Cliente;
import com.librerianova.modelo.DetalleVenta;
import com.librerianova.modelo.Producto;
import com.librerianova.modelo.Venta;
import com.librerianova.persistencia.ClienteDAO;
import com.librerianova.persistencia.ProductoDAO;
import com.librerianova.persistencia.VentaDAO;
import com.librerianova.util.GestorArchivos;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.format.DateTimeFormatter;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;

/** Pestaña de ventas: carrito, cálculo de IGV y total, registro transaccional y boleta. */
public class PanelVentas extends JPanel {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final VentaDAO ventaDAO = new VentaDAO();

    private final JComboBox<Cliente> cmbCliente = new JComboBox<>();
    private final JComboBox<Producto> cmbProducto = new JComboBox<>();
    private final JSpinner spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
    private final JTable tablaCarrito = Ui.tabla("Producto", "Cantidad", "P. unitario", "Subtotal");
    private final JTable tablaHistorial = Ui.tabla("N°", "Fecha", "Cliente", "Total");
    private final JLabel lblTotales = new JLabel("Subtotal: S/ 0.00   IGV: S/ 0.00   TOTAL: S/ 0.00");

    private Venta venta;

    public PanelVentas() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.add(new JLabel("Cliente:"));
        norte.add(cmbCliente);
        norte.add(new JLabel("Producto:"));
        norte.add(cmbProducto);
        norte.add(new JLabel("Cant.:"));
        norte.add(spnCantidad);
        JButton btnAgregar = new JButton("Agregar");
        JButton btnQuitar = new JButton("Quitar");
        norte.add(btnAgregar);
        norte.add(btnQuitar);
        add(norte, BorderLayout.NORTH);

        JPanel carrito = new JPanel(new BorderLayout());
        carrito.setBorder(BorderFactory.createTitledBorder("Carrito"));
        carrito.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pie.add(lblTotales);
        JButton btnRegistrar = new JButton("Registrar venta");
        JButton btnCancelar = new JButton("Cancelar");
        pie.add(btnRegistrar);
        pie.add(btnCancelar);
        carrito.add(pie, BorderLayout.SOUTH);

        JPanel historial = new JPanel(new BorderLayout());
        historial.setBorder(BorderFactory.createTitledBorder("Historial de ventas"));
        historial.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, carrito, historial), BorderLayout.CENTER);

        btnAgregar.addActionListener(e -> agregar());
        btnQuitar.addActionListener(e -> quitar());
        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> nuevaVenta());
        cmbCliente.addActionListener(e -> nuevaVenta());
    }

    /** Recarga clientes, productos e historial. Se llama cada vez que se abre la pestaña. */
    public void refrescar() {
        try {
            Object clientePrevio = cmbCliente.getSelectedItem();
            cmbCliente.removeAllItems();
            for (Cliente c : clienteDAO.listar()) {
                cmbCliente.addItem(c);
            }
            cmbProducto.removeAllItems();
            for (Producto p : productoDAO.listar()) {
                cmbProducto.addItem(p);
            }
            if (clientePrevio != null) {
                cmbCliente.setSelectedItem(clientePrevio);
            }
            cargarHistorial();
            nuevaVenta();
        } catch (PersistenciaException e) {
            Ui.error(this, e.getMessage());
        }
    }

    private void cargarHistorial() throws PersistenciaException {
        var modelo = Ui.modelo(tablaHistorial);
        modelo.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        for (VentaDAO.ResumenVenta r : ventaDAO.listarResumen()) {
            modelo.addRow(new Object[]{r.id(), r.fecha().format(fmt), r.cliente(), "S/ " + r.total()});
        }
    }

    private void nuevaVenta() {
        Cliente cliente = (Cliente) cmbCliente.getSelectedItem();
        venta = cliente == null ? null : new Venta(cliente);
        actualizarCarrito();
    }

    private void agregar() {
        if (venta == null || cmbProducto.getSelectedItem() == null) {
            Ui.error(this, "Registre al menos un cliente y un producto.");
            return;
        }
        try {
            venta.agregarItem((Producto) cmbProducto.getSelectedItem(), (int) spnCantidad.getValue());
            actualizarCarrito();
        } catch (DatosInvalidosException | StockInsuficienteException | IllegalStateException e) {
            Ui.error(this, e.getMessage());
        }
    }

    private void quitar() {
        int fila = tablaCarrito.getSelectedRow();
        if (venta == null || fila < 0) {
            Ui.error(this, "Seleccione un ítem del carrito.");
            return;
        }
        venta.quitarItem(tablaCarrito.convertRowIndexToModel(fila));
        actualizarCarrito();
    }

    private void actualizarCarrito() {
        var modelo = Ui.modelo(tablaCarrito);
        modelo.setRowCount(0);
        if (venta == null) {
            lblTotales.setText("Subtotal: S/ 0.00   IGV: S/ 0.00   TOTAL: S/ 0.00");
            return;
        }
        for (DetalleVenta d : venta.getDetalles()) {
            modelo.addRow(new Object[]{d.getProducto().getNombre(), d.getCantidad(),
                d.getPrecioUnitario(), d.calcularSubtotal()});
        }
        lblTotales.setText("Subtotal: S/ " + venta.calcularSubtotal() + "   IGV: S/ " + venta.calcularIGV()
                + "   TOTAL: S/ " + venta.calcularTotal());
    }

    private void registrar() {
        if (venta == null || venta.estaVacia()) {
            Ui.error(this, "El carrito está vacío.");
            return;
        }
        try {
            ventaDAO.guardar(venta);
            Venta registrada = venta;
            Ui.info(this, "Venta N° " + registrada.getId() + " registrada. Total: S/ " + registrada.calcularTotal());
            if (Ui.confirmar(this, "¿Desea guardar la boleta en un archivo?")) {
                guardarBoleta(registrada);
            }
            refrescar();
        } catch (StockInsuficienteException e) {
            Ui.error(this, e.getMessage() + "\nNo se registró la venta.");
            refrescar();
        } catch (PersistenciaException e) {
            Ui.error(this, e.getMessage());
        }
    }

    private void guardarBoleta(Venta v) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("boleta_" + v.getId() + ".txt"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                GestorArchivos.guardarBoleta(v, fc.getSelectedFile().toPath());
            } catch (PersistenciaException e) {
                Ui.error(this, e.getMessage());
            }
        }
    }
}
