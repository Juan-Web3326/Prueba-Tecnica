package com.prueba_tecnica.infrastructure.adapters.out.persistence.repository;

import com.prueba_tecnica.infrastructure.adapters.out.persistence.entity.CuentaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaJpaRepository extends JpaRepository<CuentaJpaEntity, Long> {
    boolean existsByNumeroCuenta(String numeroCuenta);

    boolean existsByClienteId(Long clienteId);
}