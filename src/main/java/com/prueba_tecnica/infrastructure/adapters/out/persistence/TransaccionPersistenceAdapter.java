package com.prueba_tecnica.infrastructure.adapters.out.persistence;

import com.prueba_tecnica.domain.model.Transaccion;
import com.prueba_tecnica.domain.ports.out.TransaccionRepositoryPort;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.entity.TransaccionJpaEntity;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.mapper.TransaccionMapper;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.repository.TransaccionJpaRepository;

import java.util.List;
import java.util.stream.Collectors;

public class TransaccionPersistenceAdapter implements TransaccionRepositoryPort {

    private final TransaccionJpaRepository transaccionJpaRepository;

    public TransaccionPersistenceAdapter(TransaccionJpaRepository transaccionJpaRepository) {
        this.transaccionJpaRepository = transaccionJpaRepository;
    }

    @Override
    public Transaccion guardar(Transaccion transaccion) {
        TransaccionJpaEntity entity = TransaccionMapper.toJpa(transaccion);
        TransaccionJpaEntity guardado = transaccionJpaRepository.save(entity);
        return TransaccionMapper.toDomain(guardado);
    }

    @Override
    public List<Transaccion> listarTodas() {
        return transaccionJpaRepository.findAll()
                .stream()
                .map(TransaccionMapper::toDomain)
                .collect(Collectors.toList());
    }
}
