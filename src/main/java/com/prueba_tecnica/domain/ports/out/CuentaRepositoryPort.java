package com.prueba_tecnica.domain.ports.out;

import com.prueba_tecnica.domain.model.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaRepositoryPort {
    Cuenta guardar(Cuenta cuenta);

    List<Cuenta> listarTodas();

    Optional<Cuenta> buscarPorId(Long id);

    boolean existePorNumeroCuenta(String numeroCuenta);

    boolean existenCuentasPorClienteId(Long clienteId);
}