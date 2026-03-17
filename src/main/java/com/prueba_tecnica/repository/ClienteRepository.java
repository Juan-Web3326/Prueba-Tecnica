package com.prueba_tecnica.repository;

import com.prueba_tecnica.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio JPA para la entidad Cliente
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Buscar cliente por número de identificación
    Optional<Cliente> findByNumeroIdentificacion(String numeroIdentificacion);

    // Verificar si ya existe un número de identificación
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);

    // Verificar si ya existe un correo
    boolean existsByCorreo(String correo);
}
