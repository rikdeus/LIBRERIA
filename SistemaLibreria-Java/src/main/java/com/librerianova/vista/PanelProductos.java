package com.librerianova.vista;

import com.librerianova.excepcion.DatosInvalidosException;
import com.librerianova.excepcion.PersistenciaException;
import com.librerianova.modelo.Libro;
import com.librerianova.modelo.Papeleria;
import com.librerianova.modelo.Producto;
import com.librerianova.persistencia.ProductoDAO;
import com.librerianova.servicio.Reportes;
import com.librerianova.util.GestorArchivos;
import com.librerianova.util.Validacion;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** Pestaña de productos: CRUD sobre la base de datos, búsqueda, importar/exportar CSV. */
public class PanelProductos extends JPanel {

    private final ProductoDAO dao = new ProductoDAO();
    private List<Producto> productos = new ArrayList<>();
    private Producto seleccionado;

    private final JTable tabla = Ui.tabla("ID", "Tipo", "Nombre", "Precio", "Stock", "Detalle");
    private final JTextField txtBuscar = new JTextField(18);
    private final JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Libro", "Papelería"});
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JSpinner spnStock = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
    private final JLabel lblExtra = new JLabel("Autor");
    private final JTextField txtExtra = new JTextField();
    private final JTextField txtIsbn = new JTextField();
    private final JSpinner spnDescuento = new JSpinner(new SpinnerNumberModel(0, 0, 50, 5));

    public PanelProductos() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel norte = new JPanel(new FlowLayout(FlowLayout.LEFT));
        norte.add(new JLabel("Buscar:"));
        norte.add(txtBuscar);
        add(norte, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Datos del producto"));
        form.add(new JLabel("Tipo"));
        form.add(cmbTipo);
        form.add(new JLabel("Nombre"));
        form.add(txtNombre);
        form.add(new JLabel("Precio (S/)"));
        form.add(txtPrecio);
        form.add(new JLabel("Stock"));
        form.add(spnStock);
        form.add(lblExtra);
        form.add(txtExtra);
        form.add(new JLabel("ISBN (libros)"));
        form.add(txtIsbn);
        form.add(new JLabel("Descuento % (libros)"));
        form.add(spnDescuento);

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnExportar = new JButton("Exportar CSV");
        JButton btnImportar = new JButton("Importar CSV");
        JButton btnStockBajo = new JButton("Stock bajo (≤5)");
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JButton b : new JButton[]{btnNuevo, btnGuardar, btnEliminar, btnExportar, btnImportar, btnStockBajo}) {
            botones.add(b);
        }

        JPanel sur = new JPanel(new BorderLayout());
        sur.add(form, BorderLayout.CENTER);
        sur.add(botones, BorderLayout.SOUTH);
        add(sur, BorderLayout.SOUTH);

        // ----- Eventos -----
        cmbTipo.addActionListener(e -> actualizarCamposPorTipo());
        btnNuevo.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        btnExportar.addActionListener(e -> exportar());
        btnImportar.addActionListener(e -> importar());
        btnStockBajo.addActionListener(e -> mostrar(Reportes.stockBajo(productos, 5)));
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { filtrar(); }
            @Override public void removeUpdate(DocumentEvent e) { filtrar(); }
            @Override public void changedUpdate(DocumentEvent e) { filtrar(); }
        });
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });

        actualizarCamposPorTipo();
        refrescar();
    }

    /** Recarga la lista desde la base de datos. */
    public void refrescar() {
        try {
            productos = dao.listar();
            mostrar(productos);
        } catch (PersistenciaException e) {
            Ui.error(this, e.getMessage());
        }
    }

    private void mostrar(List<Producto> lista) {
        var modelo = Ui.modelo(tabla);
        modelo.setRowCount(0);
        for (Producto p : lista) {
            modelo.addRow(new Object[]{p.getId(), p.getCategoria(), p.getNombre(),
                p.getPrecioFinal(), p.getStock(), p.getDetalle()});
        }
    }

    private void filtrar() {
        mostrar(Reportes.buscarPorNombre(productos, txtBuscar.getText()));
    }

    private void actualizarCamposPorTipo() {
        boolean libro = "Libro".equals(cmbTipo.getSelectedItem());
        lblExtra.setText(libro ? "Autor" : "Marca");
        txtIsbn.setEnabled(libro);
        spnDescuento.setEnabled(libro);
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        int id = (int) tabla.getValueAt(fila, 0);
        productos.stream().filter(p -> p.getId() == id).findFirst().ifPresent(p -> {
            seleccionado = p;
            txtNombre.setText(p.getNombre());
            txtPrecio.setText(p.getPrecio().toPlainString());
            spnStock.setValue(p.getStock());
            if (p instanceof Libro l) {
                cmbTipo.setSelectedItem("Libro");
                txtExtra.setText(l.getAutor());
                txtIsbn.setText(l.getIsbn());
                spnDescuento.setValue(l.getPorcentajeDescuento());
            } else if (p instanceof Papeleria pa) {
                cmbTipo.setSelectedItem("Papelería");
                txtExtra.setText(pa.getMarca());
                txtIsbn.setText("");
                spnDescuento.setValue(0);
            }
            cmbTipo.setEnabled(false); // no se cambia el tipo de un producto existente
        });
    }

    private void limpiar() {
        seleccionado = null;
        tabla.clearSelection();
        txtNombre.setText("");
        txtPrecio.setText("");
        spnStock.setValue(0);
        txtExtra.setText("");
        txtIsbn.setText("");
        spnDescuento.setValue(0);
        cmbTipo.setEnabled(true);
    }

    private void guardar() {
        try {
            String nombre = txtNombre.getText();
            var precio = Validacion.parsearPrecio(txtPrecio.getText());
            int stock = (int) spnStock.getValue();
            if (seleccionado == null) {
                Producto nuevo = "Libro".equals(cmbTipo.getSelectedItem())
                        ? new Libro(0, nombre, precio, stock, txtExtra.getText(), txtIsbn.getText(),
                                (int) spnDescuento.getValue())
                        : new Papeleria(0, nombre, precio, stock, txtExtra.getText());
                dao.guardar(nuevo);
            } else {
                seleccionado.setNombre(nombre);
                seleccionado.setPrecio(precio);
                seleccionado.setStock(stock);
                if (seleccionado instanceof Libro l) {
                    l.setAutor(txtExtra.getText());
                    l.setIsbn(txtIsbn.getText());
                    l.setPorcentajeDescuento((int) spnDescuento.getValue());
                } else if (seleccionado instanceof Papeleria pa) {
                    pa.setMarca(txtExtra.getText());
                }
                dao.actualizar(seleccionado);
            }
            limpiar();
            refrescar();
        } catch (DatosInvalidosException | PersistenciaException e) {
            Ui.error(this, e.getMessage());
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            Ui.error(this, "Seleccione un producto de la tabla.");
            return;
        }
        if (Ui.confirmar(this, "¿Eliminar \"" + seleccionado.getNombre() + "\"?")) {
            try {
                dao.eliminar(seleccionado.getId());
                limpiar();
                refrescar();
            } catch (PersistenciaException e) {
                Ui.error(this, e.getMessage());
            }
        }
    }

    private void exportar() {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("productos.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                GestorArchivos.exportarProductos(productos, fc.getSelectedFile().toPath());
                Ui.info(this, "Se exportaron " + productos.size() + " productos.");
            } catch (PersistenciaException e) {
                Ui.error(this, e.getMessage());
            }
        }
    }

    private void importar() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        Path ruta = fc.getSelectedFile().toPath();
        List<String> errores = new ArrayList<>();
        try {
            int guardados = 0;
            for (Producto p : GestorArchivos.importarProductos(ruta, errores)) {
                dao.guardar(p);
                guardados++;
            }
            refrescar();
            String msg = "Importados: " + guardados + ". Con error: " + errores.size() + ".";
            if (!errores.isEmpty()) {
                msg += "\n\n" + String.join("\n", errores.subList(0, Math.min(10, errores.size())));
            }
            Ui.info(this, msg);
        } catch (PersistenciaException e) {
            Ui.error(this, e.getMessage());
        }
    }
}
