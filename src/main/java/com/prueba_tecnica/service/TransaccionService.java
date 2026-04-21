package com.prueba_tecnica.service;

import com.prueba_tecnica.entity.Transaccion;

import java.math.BigDecimal;
import java.util.List;

// Interface del servicio de transacciones
public interface TransaccionService {

    // Consignar (depositar) dinero en una cuenta
    Transaccion consignar(Long cuentaDestinoId, BigDecimal monto);

    // Retirar dinero de una cuenta
    Transaccion retirar(Long cuentaOrigenId, BigDecimal monto);

    // Transferir dinero entre dos cuentas
    Transaccion transferir(Long cuentaOrigenId, Long cuentaDestinoId, BigDecimal monto);

    // Listar todas las transacciones
    List<Transaccion> listarTodas();
}
