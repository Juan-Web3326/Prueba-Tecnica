package com.prueba_tecnica.domain.ports.in;

import com.prueba_tecnica.domain.model.Transaccion;

import java.math.BigDecimal;
import java.util.List;

public interface TransaccionUseCase {
    Transaccion consignar(Long cuentaDestinoId, BigDecimal monto);

    Transaccion retirar(Long cuentaOrigenId, BigDecimal monto);

    Transaccion transferir(Long cuentaOrigenId, Long cuentaDestinoId, BigDecimal monto);

    List<Transaccion> listarTodas();
}