package com.prueba_tecnica.infrastructure.adapters.out.persistence.mapper;

import com.prueba_tecnica.domain.model.Transaccion;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.entity.TransaccionJpaEntity;

public class TransaccionMapper {
    public static Transaccion toDomain(TransaccionJpaEntity entity) {
        if (entity == null) return null;
        Transaccion transaccion = new Transaccion();
        transaccion.setId(entity.getId());
        transaccion.setTipoTransaccion(entity.getTipoTransaccion());
        transaccion.setCuentaOrigen(CuentaMapper.toDomain(entity.getCuentaOrigen()));
        transaccion.setCuentaDestino(CuentaMapper.toDomain(entity.getCuentaDestino()));
        transaccion.setMonto(entity.getMonto());
        transaccion.setFecha(entity.getFecha());
        return transaccion;
    }

    public static TransaccionJpaEntity toJpa(Transaccion transaccion) {
        if (transaccion == null) return null;
        TransaccionJpaEntity entity = new TransaccionJpaEntity();
        entity.setId(transaccion.getId());
        entity.setTipoTransaccion(transaccion.getTipoTransaccion());
        entity.setCuentaOrigen(CuentaMapper.toJpa(transaccion.getCuentaOrigen()));
        entity.setCuentaDestino(CuentaMapper.toJpa(transaccion.getCuentaDestino()));
        entity.setMonto(transaccion.getMonto());
        entity.setFecha(transaccion.getFecha());
        return entity;
    }
}
