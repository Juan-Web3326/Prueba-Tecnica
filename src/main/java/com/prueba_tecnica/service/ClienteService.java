package com.prueba_tecnica.service;

import com.prueba_tecnica.entity.Cliente;

import java.util.List;

// Interface del servicio de clientes
public interface ClienteService {

    // Crear cliente (valida edad >= 18)
    Cliente crear(Cliente cliente);

    // Listar todos los clientes
    List<Cliente> listarTodos();

    // Obtener cliente por id
    Cliente obtenerPorId(Long id);

    // Actualizar cliente
    Cliente actualizar(Long id, Cliente cliente);

    // Eliminar cliente (valida que no tenga cuentas)
    void eliminar(Long id);
}
