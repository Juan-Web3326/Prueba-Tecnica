package com.prueba_tecnica.infrastructure.adapters.out.persistence.repository;

import com.prueba_tecnica.infrastructure.adapters.out.persistence.entity.ClienteJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteJpaRepository extends JpaRepository<ClienteJpaEntity, Long> {
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);

    boolean existsByCorreo(String correo);
}