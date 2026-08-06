package com.prueba_tecnica.infrastructure.adapters.out.persistence.repository;

import com.prueba_tecnica.infrastructure.adapters.out.persistence.entity.TransaccionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransaccionJpaRepository extends JpaRepository<TransaccionJpaEntity, Long> {
}