package com.librerianova.vista;

import com.librerianova.excepcion.DatosInvalidosException;
import com.librerianova.excepcion.PersistenciaException;
import com.librerianova.modelo.Cliente;
import com.librerianova.persistencia.ClienteDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/** Pestaña de clientes: CRUD sobre la base de datos. */
public class PanelClientes extends JPanel {

    private final ClienteDAO dao = new ClienteDAO();
    private List<Cliente> clientes = new ArrayList<>();
    private Cliente seleccionado;

    private final JTable tabla = Ui.tabla("ID", "DNI", "Nombre", "Correo", "Teléfono");
    private final JTextField txtDni = new JTextField();
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtCorreo = new JTextField();
    private final JTextField txtTelefono = new JTextField();

    public PanelClientes() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Datos del cliente"));
        form.add(new JLabel("DNI"));
        form.add(txtDni);
        form.add(new JLabel("Nombre"));
        form.add(txtNombre);
        form.add(new JLabel("Correo"));
        form.add(txtCorreo);
        form.add(new JLabel("Teléfono"));
        form.add(txtTelefono);

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEliminar = new JButton("Eliminar");
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnNuevo);
        botones.add(btnGuardar);
        botones.add(btnEliminar);

        JPanel sur = new JPanel(new BorderLayout());
        sur.add(form, BorderLayout.CENTER);
        sur.add(botones, BorderLayout.SOUTH);
        add(sur, BorderLayout.SOUTH);

        btnNuevo.addActionListener(e -> limpiar());
        btnGuardar.addActionListener(e -> guardar());
        btnEliminar.addActionListener(e -> eliminar());
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarSeleccion();
            }
        });
        refrescar();
    }

    public void refrescar() {
        try {
            clientes = dao.listar();
            var modelo = Ui.modelo(tabla);
            modelo.setRowCount(0);
            for (Cliente c : clientes) {
                modelo.addRow(new Object[]{c.getId(), c.getDni(), c.getNombre(), c.getCorreo(), c.getTelefono()});
            }
        } catch (PersistenciaException e) {
            Ui.error(this, e.getMessage());
        }
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            return;
        }
        int id = (int) tabla.getValueAt(fila, 0);
        clientes.stream().filter(c -> c.getId() == id).findFirst().ifPresent(c -> {
            seleccionado = c;
            txtDni.setText(c.getDni());
            txtNombre.setText(c.getNombre());
            txtCorreo.setText(c.getCorreo());
            txtTelefono.setText(c.getTelefono());
        });
    }

    private void limpiar() {
        seleccionado = null;
        tabla.clearSelection();
        txtDni.setText("");
        txtNombre.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
    }

    private void guardar() {
        try {
            if (seleccionado == null) {
                dao.guardar(new Cliente(0, txtDni.getText(), txtNombre.getText(),
                        txtCorreo.getText(), txtTelefono.getText()));
            } else {
                seleccionado.setDni(txtDni.getText());
                seleccionado.setNombre(txtNombre.getText());
                seleccionado.setCorreo(txtCorreo.getText());
                seleccionado.setTelefono(txtTelefono.getText());
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
            Ui.error(this, "Seleccione un cliente de la tabla.");
            return;
        }
        if (Ui.confirmar(this, "¿Eliminar a \"" + seleccionado.getNombre() + "\"?")) {
            try {
                dao.eliminar(seleccionado.getId());
                limpiar();
                refrescar();
            } catch (PersistenciaException e) {
                Ui.error(this, e.getMessage());
            }
        }
    }
}
