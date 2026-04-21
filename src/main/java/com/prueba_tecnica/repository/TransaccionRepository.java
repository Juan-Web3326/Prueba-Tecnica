package com.prueba_tecnica.repository;

import com.prueba_tecnica.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repositorio JPA para la entidad Transaccion
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    // Buscar transacciones donde la cuenta es origen o destino
    List<Transaccion> findByCuentaOrigenIdOrCuentaDestinoId(Long cuentaOrigenId, Long cuentaDestinoId);
}
