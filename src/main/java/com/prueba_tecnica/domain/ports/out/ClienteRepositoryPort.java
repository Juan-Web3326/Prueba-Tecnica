package com.prueba_tecnica.domain.ports.out;

import com.prueba_tecnica.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryPort {
    Cliente guardar(Cliente cliente);

    List<Cliente> listarTodos();

    Optional<Cliente> buscarPorId(Long id);

    void eliminar(Long id);

    boolean existePorNumeroIdentificacion(String numeroIdentificacion);

    boolean existePorCorreo(String correo);
}