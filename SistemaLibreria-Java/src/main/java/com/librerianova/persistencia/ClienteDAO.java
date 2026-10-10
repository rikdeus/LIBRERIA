package com.librerianova.persistencia;

import com.librerianova.excepcion.PersistenciaException;
import com.librerianova.modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAO implements Repositorio<Cliente> {

    @Override
    public Cliente guardar(Cliente c) throws PersistenciaException {
        String sql = "INSERT INTO cliente (dni, nombre, correo, telefono) VALUES (?, ?, ?, ?)";
        try (Connection cn = Conexion.obtener();
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getCorreo());
            ps.setString(4, c.getTelefono());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    c.setId(keys.getInt(1));
                }
            }
            return c;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo guardar el cliente (¿el DNI ya existe?).", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(int id) throws PersistenciaException {
        try (Connection cn = Conexion.obtener();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM cliente WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo consultar el cliente.", e);
        }
    }

    @Override
    public List<Cliente> listar() throws PersistenciaException {
        List<Cliente> lista = new ArrayList<>();
        try (Connection cn = Conexion.obtener();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM cliente ORDER BY nombre")) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo listar los clientes.", e);
        }
    }

    @Override
    public void actualizar(Cliente c) throws PersistenciaException {
        String sql = "UPDATE cliente SET dni=?, nombre=?, correo=?, telefono=? WHERE id=?";
        try (Connection cn = Conexion.obtener(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getDni());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getCorreo());
            ps.setString(4, c.getTelefono());
            ps.setInt(5, c.getId());
            if (ps.executeUpdate() == 0) {
                throw new PersistenciaException("El cliente ya no existe.");
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el cliente (¿el DNI ya existe?).", e);
        }
    }

    @Override
    public void eliminar(int id) throws PersistenciaException {
        try (Connection cn = Conexion.obtener();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM cliente WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo eliminar el cliente (puede tener ventas registradas).", e);
        }
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        return new Cliente(rs.getInt("id"), rs.getString("dni"), rs.getString("nombre"),
                rs.getString("correo"), rs.getString("telefono"));
    }
}
