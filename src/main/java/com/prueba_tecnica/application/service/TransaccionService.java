package com.prueba_tecnica.application.service;

import com.prueba_tecnica.domain.model.Cuenta;
import com.prueba_tecnica.domain.model.Transaccion;
import com.prueba_tecnica.domain.ports.in.TransaccionUseCase;
import com.prueba_tecnica.domain.ports.out.CuentaRepositoryPort;
import com.prueba_tecnica.domain.ports.out.TransaccionRepositoryPort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransaccionService implements TransaccionUseCase {

    private final TransaccionRepositoryPort transaccionRepositoryPort;
    private final CuentaRepositoryPort cuentaRepositoryPort;

    public TransaccionService(TransaccionRepositoryPort transaccionRepositoryPort, CuentaRepositoryPort cuentaRepositoryPort) {
        this.transaccionRepositoryPort = transaccionRepositoryPort;
        this.cuentaRepositoryPort = cuentaRepositoryPort;
    }

    @Override
    public Transaccion consignar(Long cuentaDestinoId, BigDecimal monto) {
        validarMonto(monto);
        Cuenta cuentaDestino = buscarCuentaActiva(cuentaDestinoId);

        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));
        cuentaDestino.setSaldoDisponible(cuentaDestino.getSaldoDisponible().add(monto));
        cuentaRepositoryPort.guardar(cuentaDestino);

        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion("CONSIGNACION");
        transaccion.setCuentaDestino(cuentaDestino);
        transaccion.setMonto(monto);
        transaccion.setFecha(LocalDateTime.now());

        return transaccionRepositoryPort.guardar(transaccion);
    }

    @Override
    public Transaccion retirar(Long cuentaOrigenId, BigDecimal monto) {
        validarMonto(monto);
        Cuenta cuentaOrigen = buscarCuentaActiva(cuentaOrigenId);
        validarSaldoSuficiente(cuentaOrigen, monto);

        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
        cuentaOrigen.setSaldoDisponible(cuentaOrigen.getSaldoDisponible().subtract(monto));
        cuentaRepositoryPort.guardar(cuentaOrigen);

        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion("RETIRO");
        transaccion.setCuentaOrigen(cuentaOrigen);
        transaccion.setMonto(monto);
        transaccion.setFecha(LocalDateTime.now());

        return transaccionRepositoryPort.guardar(transaccion);
    }

    @Override
    public Transaccion transferir(Long cuentaOrigenId, Long cuentaDestinoId, BigDecimal monto) {
        validarMonto(monto);
        if (cuentaOrigenId.equals(cuentaDestinoId)) {
            throw new RuntimeException("No se puede transferir a la misma cuenta");
        }

        Cuenta cuentaOrigen = buscarCuentaActiva(cuentaOrigenId);
        Cuenta cuentaDestino = buscarCuentaActiva(cuentaDestinoId);
        validarSaldoSuficiente(cuentaOrigen, monto);

        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
        cuentaOrigen.setSaldoDisponible(cuentaOrigen.getSaldoDisponible().subtract(monto));
        cuentaRepositoryPort.guardar(cuentaOrigen);

        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));
        cuentaDestino.setSaldoDisponible(cuentaDestino.getSaldoDisponible().add(monto));
        cuentaRepositoryPort.guardar(cuentaDestino);

        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion("TRANSFERENCIA");
        transaccion.setCuentaOrigen(cuentaOrigen);
        transaccion.setCuentaDestino(cuentaDestino);
        transaccion.setMonto(monto);
        transaccion.setFecha(LocalDateTime.now());

        return transaccionRepositoryPort.guardar(transaccion);
    }

    @Override
    public List<Transaccion> listarTodas() {
        return transaccionRepositoryPort.listarTodas();
    }

    private Cuenta buscarCuentaActiva(Long cuentaId) {
        Cuenta cuenta = cuentaRepositoryPort.buscarPorId(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + cuentaId));
        if (!"ACTIVA".equals(cuenta.getEstado())) {
            throw new RuntimeException("La cuenta no está activa");
        }
        return cuenta;
    }

    private void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto debe ser mayor a $0");
        }
    }

    private void validarSaldoSuficiente(Cuenta cuenta, BigDecimal monto) {
        if ("AHORROS".equals(cuenta.getTipoCuenta()) && cuenta.getSaldo().compareTo(monto) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }
    }
}