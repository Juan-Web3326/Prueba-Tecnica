package com.prueba_tecnica.domain.ports.in;

import com.prueba_tecnica.domain.model.Cuenta;

import java.util.List;

public interface CuentaUseCase {
    Cuenta crear(Cuenta cuenta, Long clienteId);

    List<Cuenta> listarTodas();

    Cuenta obtenerPorId(Long id);

    Cuenta activar(Long id);

    Cuenta inactivar(Long id);

    Cuenta cancelar(Long id);
}