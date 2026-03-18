package com.prueba_tecnica.service;

import com.prueba_tecnica.entity.Cuenta;

import java.util.List;

// Interface del servicio de cuentas (productos)
public interface CuentaService {

    // Crear cuenta vinculada a un cliente
    Cuenta crear(Cuenta cuenta, Long clienteId);

    // Listar todas las cuentas
    List<Cuenta> listarTodas();

    // Obtener cuenta por id
    Cuenta obtenerPorId(Long id);

    // Activar cuenta
    Cuenta activar(Long id);

    // Inactivar cuenta
    Cuenta inactivar(Long id);

    // Cancelar cuenta (solo si saldo = 0)
    Cuenta cancelar(Long id);
}
