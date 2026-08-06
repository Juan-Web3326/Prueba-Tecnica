package com.prueba_tecnica.domain.ports.out;

import com.prueba_tecnica.domain.model.Transaccion;

import java.util.List;

public interface TransaccionRepositoryPort {
    Transaccion guardar(Transaccion transaccion);

    List<Transaccion> listarTodas();
}