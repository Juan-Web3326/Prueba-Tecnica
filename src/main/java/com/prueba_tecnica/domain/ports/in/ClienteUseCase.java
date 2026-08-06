package com.prueba_tecnica.domain.ports.in;

import com.prueba_tecnica.domain.model.Cliente;

import java.util.List;

public interface ClienteUseCase {
    Cliente crear(Cliente cliente);

    List<Cliente> listarTodos();

    Cliente obtenerPorId(Long id);

    Cliente actualizar(Long id, Cliente cliente);

    void eliminar(Long id);
}