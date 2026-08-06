package com.prueba_tecnica.infrastructure.adapters.out.persistence;

import com.prueba_tecnica.domain.model.Cuenta;
import com.prueba_tecnica.domain.ports.out.CuentaRepositoryPort;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.entity.CuentaJpaEntity;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.mapper.CuentaMapper;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.repository.CuentaJpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CuentaPersistenceAdapter implements CuentaRepositoryPort {

    private final CuentaJpaRepository cuentaJpaRepository;

    public CuentaPersistenceAdapter(CuentaJpaRepository cuentaJpaRepository) {
        this.cuentaJpaRepository = cuentaJpaRepository;
    }

    @Override
    public Cuenta guardar(Cuenta cuenta) {
        CuentaJpaEntity entity = CuentaMapper.toJpa(cuenta);
        CuentaJpaEntity guardado = cuentaJpaRepository.save(entity);
        return CuentaMapper.toDomain(guardado);
    }

    @Override
    public List<Cuenta> listarTodas() {
        return cuentaJpaRepository.findAll()
                .stream()
                .map(CuentaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Cuenta> buscarPorId(Long id) {
        return cuentaJpaRepository.findById(id)
                .map(CuentaMapper::toDomain);
    }

    @Override
    public boolean existePorNumeroCuenta(String numeroCuenta) {
        return cuentaJpaRepository.existsByNumeroCuenta(numeroCuenta);
    }

    @Override
    public boolean existenCuentasPorClienteId(Long clienteId) {
        return cuentaJpaRepository.existsByClienteId(clienteId);
    }
}
