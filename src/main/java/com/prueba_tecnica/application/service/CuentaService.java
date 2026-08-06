package com.prueba_tecnica.application.service;

import com.prueba_tecnica.domain.model.Cliente;
import com.prueba_tecnica.domain.model.Cuenta;
import com.prueba_tecnica.domain.ports.in.CuentaUseCase;
import com.prueba_tecnica.domain.ports.out.ClienteRepositoryPort;
import com.prueba_tecnica.domain.ports.out.CuentaRepositoryPort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CuentaService implements CuentaUseCase {

    private final CuentaRepositoryPort cuentaRepositoryPort;
    private final ClienteRepositoryPort clienteRepositoryPort;

    public CuentaService(CuentaRepositoryPort cuentaRepositoryPort, ClienteRepositoryPort clienteRepositoryPort) {
        this.cuentaRepositoryPort = cuentaRepositoryPort;
        this.clienteRepositoryPort = clienteRepositoryPort;
    }

    @Override
    public Cuenta crear(Cuenta cuenta, Long clienteId) {
        Cliente cliente = clienteRepositoryPort.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + clienteId));

        cuenta.setCliente(cliente);

        if ("AHORROS".equals(cuenta.getTipoCuenta())
                && cuenta.getSaldo() != null
                && cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("La cuenta de ahorros no puede tener saldo menor a $0");
        }

        cuenta.setEstado("ACTIVA");
        if (cuenta.getSaldo() == null) {
            cuenta.setSaldo(BigDecimal.ZERO);
        }
        cuenta.setSaldoDisponible(cuenta.getSaldo());
        cuenta.setNumeroCuenta(generarNumeroCuenta(cuenta.getTipoCuenta()));
        cuenta.setFechaCreacion(LocalDateTime.now());

        return cuentaRepositoryPort.guardar(cuenta);
    }

    @Override
    public List<Cuenta> listarTodas() {
        return cuentaRepositoryPort.listarTodas();
    }

    @Override
    public Cuenta obtenerPorId(Long id) {
        return cuentaRepositoryPort.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + id));
    }

    @Override
    public Cuenta activar(Long id) {
        Cuenta cuenta = obtenerPorId(id);
        cuenta.setEstado("ACTIVA");
        cuenta.setFechaModificacion(LocalDateTime.now());
        return cuentaRepositoryPort.guardar(cuenta);
    }

    @Override
    public Cuenta inactivar(Long id) {
        Cuenta cuenta = obtenerPorId(id);
        cuenta.setEstado("INACTIVA");
        cuenta.setFechaModificacion(LocalDateTime.now());
        return cuentaRepositoryPort.guardar(cuenta);
    }

    @Override
    public Cuenta cancelar(Long id) {
        Cuenta cuenta = obtenerPorId(id);
        if (cuenta.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Solo se puede cancelar una cuenta con saldo igual a $0");
        }
        cuenta.setEstado("CANCELADA");
        cuenta.setFechaModificacion(LocalDateTime.now());
        return cuentaRepositoryPort.guardar(cuenta);
    }

    private String generarNumeroCuenta(String tipoCuenta) {
        String prefijo = "AHORROS".equals(tipoCuenta) ? "53" : "33";
        String numeroCuenta;
        do {
            int numeroAleatorio = (int) (Math.random() * 100_000_000);
            numeroCuenta = prefijo + String.format("%08d", numeroAleatorio);
        } while (cuentaRepositoryPort.existePorNumeroCuenta(numeroCuenta));
        return numeroCuenta;
    }
}