package com.prueba_tecnica.infrastructure.adapters.out.persistence.mapper;

import com.prueba_tecnica.domain.model.Cuenta;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.entity.CuentaJpaEntity;

public class CuentaMapper {

    public static Cuenta toDomain(CuentaJpaEntity entity) {
        if (entity == null) return null;
        Cuenta cuenta = new Cuenta();
        cuenta.setId(entity.getId());
        cuenta.setCliente(ClienteMapper.toDomain(entity.getCliente()));
        cuenta.setTipoCuenta(entity.getTipoCuenta());
        cuenta.setNumeroCuenta(entity.getNumeroCuenta());
        cuenta.setEstado(entity.getEstado());
        cuenta.setSaldo(entity.getSaldo());
        cuenta.setSaldoDisponible(entity.getSaldoDisponible());
        cuenta.setExentaGmf(entity.getExentaGmf());
        cuenta.setFechaCreacion(entity.getFechaCreacion());
        cuenta.setFechaModificacion(entity.getFechaModificacion());
        return cuenta;
    }

    public static CuentaJpaEntity toJpa(Cuenta cuenta) {
        if (cuenta == null) return null;
        CuentaJpaEntity entity = new CuentaJpaEntity();
        entity.setId(cuenta.getId());
        entity.setCliente(ClienteMapper.toJpa(cuenta.getCliente()));
        entity.setTipoCuenta(cuenta.getTipoCuenta());
        entity.setNumeroCuenta(cuenta.getNumeroCuenta());
        entity.setEstado(cuenta.getEstado());
        entity.setSaldo(cuenta.getSaldo());
        entity.setSaldoDisponible(cuenta.getSaldoDisponible());
        entity.setExentaGmf(cuenta.getExentaGmf());
        entity.setFechaCreacion(cuenta.getFechaCreacion());
        entity.setFechaModificacion(cuenta.getFechaModificacion());
        return entity;
    }
}
