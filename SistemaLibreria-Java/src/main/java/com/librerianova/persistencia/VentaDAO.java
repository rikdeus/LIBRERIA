package com.librerianova.persistencia;

import com.librerianova.excepcion.PersistenciaException;
import com.librerianova.excepcion.StockInsuficienteException;
import com.librerianova.modelo.DetalleVenta;
import com.librerianova.modelo.Venta;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO {

    /** Fila resumida para mostrar el historial. */
    public record ResumenVenta(int id, LocalDateTime fecha, String cliente, BigDecimal total) {
    }

    /**
     * Guarda la venta y descuenta el stock en UNA transacción:
     * si algo falla (por ejemplo, stock insuficiente) no se guarda nada.
     */
    public void guardar(Venta venta) throws PersistenciaException {
        if (venta.estaVacia()) {
            throw new PersistenciaException("La venta no tiene productos.");
        }
        try (Connection cn = Conexion.obtener()) {
            cn.setAutoCommit(false);
            try {
                insertarVenta(cn, venta);
                for (DetalleVenta d : venta.getDetalles()) {
                    insertarDetalle(cn, venta.getId(), d);
                    descontarStock(cn, d);
                }
                cn.commit();
            } catch (SQLException | RuntimeException e) {
                cn.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo registrar la venta.", e);
        }
    }

    private void insertarVenta(Connection cn, Venta venta) throws SQLException {
        String sql = "INSERT INTO venta (cliente_id, fecha, subtotal, igv, total) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, venta.getCliente().getId());
            ps.setTimestamp(2, Timestamp.valueOf(venta.getFecha()));
            ps.setBigDecimal(3, venta.calcularSubtotal());
            ps.setBigDecimal(4, venta.calcularIGV());
            ps.setBigDecimal(5, venta.calcularTotal());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    venta.setId(keys.getInt(1));
                }
            }
        }
    }

    private void insertarDetalle(Connection cn, int ventaId, DetalleVenta d) throws SQLException {
        String sql = "INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, ventaId);
            ps.setInt(2, d.getProducto().getId());
            ps.setInt(3, d.getCantidad());
            ps.setBigDecimal(4, d.getPrecioUnitario());
            ps.executeUpdate();
        }
    }

    private void descontarStock(Connection cn, DetalleVenta d) throws SQLException {
        String sql = "UPDATE producto SET stock = stock - ? WHERE id = ? AND stock >= ?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, d.getCantidad());
            ps.setInt(2, d.getProducto().getId());
            ps.setInt(3, d.getCantidad());
            if (ps.executeUpdate() == 0) {
                throw new StockInsuficienteException(d.getProducto().getNombre(), stockActual(cn, d));
            }
        }
    }

    private int stockActual(Connection cn, DetalleVenta d) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("SELECT stock FROM producto WHERE id = ?")) {
            ps.setInt(1, d.getProducto().getId());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public List<ResumenVenta> listarResumen() throws PersistenciaException {
        String sql = "SELECT v.id, v.fecha, c.nombre, v.total FROM venta v "
                + "JOIN cliente c ON c.id = v.cliente_id ORDER BY v.fecha DESC";
        List<ResumenVenta> lista = new ArrayList<>();
        try (Connection cn = Conexion.obtener();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new ResumenVenta(rs.getInt(1), rs.getTimestamp(2).toLocalDateTime(),
                        rs.getString(3), rs.getBigDecimal(4)));
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo consultar el historial de ventas.", e);
        }
    }
}
