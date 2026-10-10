package com.librerianova.persistencia;

import com.librerianova.excepcion.PersistenciaException;
import java.util.List;
import java.util.Optional;

/** CRUD genérico. T es el tipo de entidad (Producto, Cliente...). */
public interface Repositorio<T> {
    T guardar(T entidad) throws PersistenciaException;

    Optional<T> buscarPorId(int id) throws PersistenciaException;

    List<T> listar() throws PersistenciaException;

    void actualizar(T entidad) throws PersistenciaException;

    void eliminar(int id) throws PersistenciaException;
}
