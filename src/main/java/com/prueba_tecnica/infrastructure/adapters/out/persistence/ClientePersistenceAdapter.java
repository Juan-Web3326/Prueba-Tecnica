package com.prueba_tecnica.infrastructure.adapters.out.persistence;

import com.prueba_tecnica.domain.model.Cliente;
import com.prueba_tecnica.domain.ports.out.ClienteRepositoryPort;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.entity.ClienteJpaEntity;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.mapper.ClienteMapper;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.repository.ClienteJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClientePersistenceAdapter implements ClienteRepositoryPort {
    private final ClienteJpaRepository clienteJpaRepository;

    public ClientePersistenceAdapter(ClienteJpaRepository clienteJpaRepository) {
        this.clienteJpaRepository = clienteJpaRepository;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        ClienteJpaEntity entity = ClienteMapper.toJpa(cliente);
        ClienteJpaEntity guardado = clienteJpaRepository.save(entity);
        return ClienteMapper.toDomain(guardado);
    }

    @Override
    public List<Cliente> listarTodos() {
        return clienteJpaRepository.findAll()
                .stream()
                .map(ClienteMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteJpaRepository.findById(id)
                .map(ClienteMapper::toDomain);
    }

    @Override
    public void eliminar(Long id) {
        clienteJpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorNumeroIdentificacion(String numeroIdentificacion) {
        return clienteJpaRepository.existsByNumeroIdentificacion(numeroIdentificacion);
    }

    @Override
    public boolean existePorCorreo(String correo) {
        return clienteJpaRepository.existsByCorreo(correo);
    }
}
