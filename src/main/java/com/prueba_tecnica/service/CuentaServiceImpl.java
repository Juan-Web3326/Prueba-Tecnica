package com.prueba_tecnica.service;

import com.prueba_tecnica.entity.Cliente;
import com.prueba_tecnica.entity.Cuenta;
import com.prueba_tecnica.entity.Cuenta.EstadoCuenta;
import com.prueba_tecnica.entity.Cuenta.TipoCuenta;
import com.prueba_tecnica.repository.ClienteRepository;
import com.prueba_tecnica.repository.CuentaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

// Implementación del servicio de cuentas (productos)
@Service
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    // Crear cuenta vinculada a un cliente existente
    @Override
    public Cuenta crear(Cuenta cuenta, Long clienteId) {
        // Validar que el cliente exista
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + clienteId));

        // Vincular la cuenta al cliente
        cuenta.setCliente(cliente);

        // Validar que cuenta de ahorros no tenga saldo negativo
        if (cuenta.getTipoCuenta() == TipoCuenta.AHORROS
                && cuenta.getSaldo() != null
                && cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("La cuenta de ahorros no puede tener saldo menor a $0");
        }

        // Si es cuenta de ahorros, se activa por defecto
        if (cuenta.getTipoCuenta() == TipoCuenta.AHORROS) {
            cuenta.setEstado(EstadoCuenta.ACTIVA);
        }

        // Si no se envía saldo, iniciar en 0
        if (cuenta.getSaldo() == null) {
            cuenta.setSaldo(BigDecimal.ZERO);
        }

        // Generar número de cuenta único de 10 dígitos
        cuenta.setNumeroCuenta(generarNumeroCuenta(cuenta.getTipoCuenta()));

        return cuentaRepository.save(cuenta);
    }

    // Listar todas las cuentas
    @Override
    public List<Cuenta> listarTodas() {
        return cuentaRepository.findAll();
    }

    // Obtener cuenta por id
    @Override
    public Cuenta obtenerPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + id));
    }

    // Activar cuenta en cualquier momento
    @Override
    public Cuenta activar(Long id) {
        Cuenta cuenta = obtenerPorId(id);
        cuenta.setEstado(EstadoCuenta.ACTIVA);
        return cuentaRepository.save(cuenta);
    }

    // Inactivar cuenta en cualquier momento
    @Override
    public Cuenta inactivar(Long id) {
        Cuenta cuenta = obtenerPorId(id);
        cuenta.setEstado(EstadoCuenta.INACTIVA);
        return cuentaRepository.save(cuenta);
    }

    // Cancelar cuenta — solo si saldo es $0
    @Override
    public Cuenta cancelar(Long id) {
        Cuenta cuenta = obtenerPorId(id);

        if (cuenta.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("Solo se puede cancelar una cuenta con saldo igual a $0");
        }

        cuenta.setEstado(EstadoCuenta.CANCELADA);
        return cuentaRepository.save(cuenta);
    }

    // Generar número de cuenta único de 10 dígitos
    // Ahorros empieza con "53", Corriente empieza con "33"
    private String generarNumeroCuenta(TipoCuenta tipoCuenta) {
        String prefijo = (tipoCuenta == TipoCuenta.AHORROS) ? "53" : "33";

        String numeroCuenta;
        do {
            int numeroAleatorio = (int) (Math.random() * 100_000_000);
            numeroCuenta = prefijo + String.format("%08d", numeroAleatorio);
        } while (cuentaRepository.existsByNumeroCuenta(numeroCuenta));

        return numeroCuenta;
    }
}
