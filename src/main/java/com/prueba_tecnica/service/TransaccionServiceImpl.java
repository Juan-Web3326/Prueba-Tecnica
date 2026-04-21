package com.prueba_tecnica.service;

import com.prueba_tecnica.entity.Cuenta;
import com.prueba_tecnica.entity.Cuenta.EstadoCuenta;
import com.prueba_tecnica.entity.Cuenta.TipoCuenta;
import com.prueba_tecnica.entity.Transaccion;
import com.prueba_tecnica.entity.Transaccion.TipoTransaccion;
import com.prueba_tecnica.repository.CuentaRepository;
import com.prueba_tecnica.repository.TransaccionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

// Implementación del servicio de transacciones con toda la lógica de negocio
@Service
public class TransaccionServiceImpl implements TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;

    public TransaccionServiceImpl(TransaccionRepository transaccionRepository,
                                   CuentaRepository cuentaRepository) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaRepository = cuentaRepository;
    }

    // ==================== CONSIGNACIÓN ====================
    // El dinero entra al sistema → se suma al saldo de la cuenta destino
    @Override
    @Transactional
    public Transaccion consignar(Long cuentaDestinoId, BigDecimal monto) {
        // Validar que el monto sea positivo
        validarMonto(monto);

        // Buscar la cuenta destino
        Cuenta cuentaDestino = buscarCuentaActiva(cuentaDestinoId);

        // Sumar el monto al saldo de la cuenta destino
        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));
        cuentaDestino.setSaldoDisponible(cuentaDestino.getSaldoDisponible().add(monto));
        cuentaRepository.save(cuentaDestino);

        // Registrar la transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion(TipoTransaccion.CONSIGNACION);
        transaccion.setCuentaDestino(cuentaDestino);
        // cuentaOrigen queda null (el dinero viene de fuera)
        transaccion.setMonto(monto);

        return transaccionRepository.save(transaccion);
    }

    // ==================== RETIRO ====================
    // El dinero sale del sistema → se resta del saldo de la cuenta origen
    @Override
    @Transactional
    public Transaccion retirar(Long cuentaOrigenId, BigDecimal monto) {
        // Validar que el monto sea positivo
        validarMonto(monto);

        // Buscar la cuenta origen
        Cuenta cuentaOrigen = buscarCuentaActiva(cuentaOrigenId);

        // Validar saldo suficiente según tipo de cuenta
        validarSaldoSuficiente(cuentaOrigen, monto);

        // Restar el monto del saldo de la cuenta origen
        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
        cuentaOrigen.setSaldoDisponible(cuentaOrigen.getSaldoDisponible().subtract(monto));
        cuentaRepository.save(cuentaOrigen);

        // Registrar la transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion(TipoTransaccion.RETIRO);
        transaccion.setCuentaOrigen(cuentaOrigen);
        // cuentaDestino queda null (el dinero sale del sistema)
        transaccion.setMonto(monto);

        return transaccionRepository.save(transaccion);
    }

    // ==================== TRANSFERENCIA ====================
    // El dinero se mueve de una cuenta a otra dentro del sistema
    @Override
    @Transactional
    public Transaccion transferir(Long cuentaOrigenId, Long cuentaDestinoId, BigDecimal monto) {
        // Validar que el monto sea positivo
        validarMonto(monto);

        // Validar que no se transfiera a la misma cuenta
        if (cuentaOrigenId.equals(cuentaDestinoId)) {
            throw new RuntimeException("No se puede transferir a la misma cuenta");
        }

        // Buscar ambas cuentas (deben existir y estar activas)
        Cuenta cuentaOrigen = buscarCuentaActiva(cuentaOrigenId);
        Cuenta cuentaDestino = buscarCuentaActiva(cuentaDestinoId);

        // Validar saldo suficiente en la cuenta origen
        validarSaldoSuficiente(cuentaOrigen, monto);

        // Débito: restar de la cuenta origen
        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo().subtract(monto));
        cuentaOrigen.setSaldoDisponible(cuentaOrigen.getSaldoDisponible().subtract(monto));
        cuentaRepository.save(cuentaOrigen);

        // Crédito: sumar a la cuenta destino
        cuentaDestino.setSaldo(cuentaDestino.getSaldo().add(monto));
        cuentaDestino.setSaldoDisponible(cuentaDestino.getSaldoDisponible().add(monto));
        cuentaRepository.save(cuentaDestino);

        // Registrar la transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setTipoTransaccion(TipoTransaccion.TRANSFERENCIA);
        transaccion.setCuentaOrigen(cuentaOrigen);
        transaccion.setCuentaDestino(cuentaDestino);
        transaccion.setMonto(monto);

        return transaccionRepository.save(transaccion);
    }

    // ==================== LISTAR ====================
    @Override
    public List<Transaccion> listarTodas() {
        return transaccionRepository.findAll();
    }

    // ==================== MÉTODOS AUXILIARES ====================

    // Buscar una cuenta que exista y esté ACTIVA
    private Cuenta buscarCuentaActiva(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + cuentaId));

        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            throw new RuntimeException("La cuenta " + cuentaId + " no está activa. Estado actual: " + cuenta.getEstado());
        }

        return cuenta;
    }

    // Validar que el monto sea mayor a 0
    private void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto debe ser mayor a $0");
        }
    }

    // Validar saldo suficiente según tipo de cuenta
    // - Ahorros: NO puede quedar con saldo < $0
    // - Corriente: SÍ puede quedar en negativo (sobregiro)
    private void validarSaldoSuficiente(Cuenta cuenta, BigDecimal monto) {
        if (cuenta.getTipoCuenta() == TipoCuenta.AHORROS) {
            if (cuenta.getSaldo().compareTo(monto) < 0) {
                throw new RuntimeException(
                        "Saldo insuficiente. Saldo actual: $" + cuenta.getSaldo()
                                + ", Monto a debitar: $" + monto);
            }
        }
        // Cuenta corriente permite sobregiro, no se valida
    }
}
