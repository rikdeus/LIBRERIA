package com.librerianova.persistencia;

import com.librerianova.excepcion.PersistenciaException;
import com.librerianova.modelo.Libro;
import com.librerianova.modelo.Papeleria;
import com.librerianova.modelo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoDAO implements Repositorio<Producto> {

    @Override
    public Producto guardar(Producto p) throws PersistenciaException {
        String sql = "INSERT INTO producto (tipo, nombre, precio, stock, autor, isbn, descuento, marca) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection cn = Conexion.obtener();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            asignar(ps, p);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getInt(1));
                }
            }
            return p;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar el producto.", e);
        }
    }

    @Override
    public Optional<Producto> buscarPorId(int id) throws PersistenciaException {
        try (Connection cn = Conexion.obtener();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM producto WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo consultar el producto.", e);
        }
    }

    @Override
    public List<Producto> listar() throws PersistenciaException {
        List<Producto> lista = new ArrayList<>();
        try (Connection cn = Conexion.obtener();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM producto ORDER BY nombre")) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar los productos.", e);
        }
    }

    @Override
    public void actualizar(Producto p) throws PersistenciaException {
        String sql = "UPDATE producto SET tipo=?, nombre=?, precio=?, stock=?, autor=?, isbn=?, descuento=?, marca=? "
                + "WHERE id=?";
        try (Connection cn = Conexion.obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            asignar(ps, p);
            ps.setInt(9, p.getId());
            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("El producto ya no existe.");
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el producto.", e);
        }
    }

    @Override
    public void eliminar(int id) throws PersistenciaException {
        try (Connection cn = Conexion.obtener();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM producto WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo eliminar el producto (puede tener ventas registradas).", e);
        }
    }

    private void asignar(PreparedStatement ps, Producto p) throws SQLException {
        ps.setString(1, p.getCategoria());
        ps.setString(2, p.getNombre());
        ps.setBigDecimal(3, p.getPrecio());
        ps.setInt(4, p.getStock());
        if (p instanceof Libro l) {
            ps.setString(5, l.getAutor());
            ps.setString(6, l.getIsbn());
            ps.setInt(7, l.getPorcentajeDescuento());
            ps.setNull(8, java.sql.Types.VARCHAR);
        } else if (p instanceof Papeleria pa) {
            ps.setNull(5, java.sql.Types.VARCHAR);
            ps.setNull(6, java.sql.Types.VARCHAR);
            ps.setInt(7, 0);
            ps.setString(8, pa.getMarca());
        }
    }

    private Producto mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        if ("Libro".equals(rs.getString("tipo"))) {
            return new Libro(id, rs.getString("nombre"), rs.getBigDecimal("precio"), rs.getInt("stock"),
                    rs.getString("autor"), rs.getString("isbn"), rs.getInt("descuento"));
        }
        return new Papeleria(id, rs.getString("nombre"), rs.getBigDecimal("precio"),
                rs.getInt("stock"), rs.getString("marca"));
    }
}
