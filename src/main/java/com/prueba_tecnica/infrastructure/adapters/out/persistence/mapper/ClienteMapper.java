package com.prueba_tecnica.infrastructure.adapters.out.persistence.mapper;

import com.prueba_tecnica.domain.model.Cliente;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.entity.ClienteJpaEntity;

public class ClienteMapper {

    // Convierte la entidad JPA → objeto puro del Dominio
    public static Cliente toDomain(ClienteJpaEntity entity) {
        if (entity == null) return null;
        Cliente cliente = new Cliente();
        cliente.setId(entity.getId());
        cliente.setTipoIdentificacion(entity.getTipoIdentificacion());
        cliente.setNumeroIdentificacion(entity.getNumeroIdentificacion());
        cliente.setNombres(entity.getNombres());
        cliente.setApellidos(entity.getApellidos());
        cliente.setCorreo(entity.getCorreo());
        cliente.setFechaNacimiento(entity.getFechaNacimiento());
        cliente.setFechaCreacion(entity.getFechaCreacion());
        cliente.setFechaModificacion(entity.getFechaModificacion());
        return cliente;
    }

    // Convierte el objeto puro del Dominio → entidad JPA
    public static ClienteJpaEntity toJpa(Cliente cliente) {
        if (cliente == null) return null;
        ClienteJpaEntity entity = new ClienteJpaEntity();
        entity.setId(cliente.getId());
        entity.setTipoIdentificacion(cliente.getTipoIdentificacion());
        entity.setNumeroIdentificacion(cliente.getNumeroIdentificacion());
        entity.setNombres(cliente.getNombres());
        entity.setApellidos(cliente.getApellidos());
        entity.setCorreo(cliente.getCorreo());
        entity.setFechaNacimiento(cliente.getFechaNacimiento());
        entity.setFechaCreacion(cliente.getFechaCreacion());
        entity.setFechaModificacion(cliente.getFechaModificacion());
        return entity;
    }
}