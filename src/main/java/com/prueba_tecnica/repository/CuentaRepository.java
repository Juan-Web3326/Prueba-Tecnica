package com.prueba_tecnica.repository;

import com.prueba_tecnica.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio JPA para la entidad Cuenta
@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    // Verificar si un cliente tiene cuentas asociadas
    boolean existsByClienteId(Long clienteId);

    // Verificar si ya existe un número de cuenta
    boolean existsByNumeroCuenta(String numeroCuenta);

    // Listar cuentas de un cliente
    java.util.List<Cuenta> findByClienteId(Long clienteId);
}
