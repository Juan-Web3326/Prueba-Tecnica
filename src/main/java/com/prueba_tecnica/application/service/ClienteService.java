package com.prueba_tecnica.application.service;

import com.prueba_tecnica.domain.model.Cliente;
import com.prueba_tecnica.domain.ports.in.ClienteUseCase;
import com.prueba_tecnica.domain.ports.out.ClienteRepositoryPort;
import com.prueba_tecnica.domain.ports.out.CuentaRepositoryPort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

public class ClienteService implements ClienteUseCase {
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final CuentaRepositoryPort cuentaRepositoryPort;

    // Inyección de dependencias por constructor tradicional (sin @Autowired)
    public ClienteService(ClienteRepositoryPort clienteRepositoryPort, CuentaRepositoryPort cuentaRepositoryPort) {
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.cuentaRepositoryPort = cuentaRepositoryPort;
    }

    @Override
    public Cliente crear(Cliente cliente) {
        int edad = Period.between(cliente.getFechaNacimiento(), LocalDate.now()).getYears();
        if (edad < 18) {
            throw new RuntimeException("El cliente deber ser mayor de edad minimo 18 años");
        }
        if (clienteRepositoryPort.existePorNumeroIdentificacion(cliente.getNumeroIdentificacion())) {
            throw new RuntimeException("Ya existe un cliente con ese numero de identificacion");
        }
        if (clienteRepositoryPort.existePorCorreo(cliente.getCorreo())) {
            throw new RuntimeException("Ya existe un cliente con ese correo");
        }

        cliente.setFechaCreacion(LocalDateTime.now());
        return clienteRepositoryPort.guardar(cliente);
    }

    @Override
    public List<Cliente> listarTodos() {
        return clienteRepositoryPort.listarTodos();
    }

    @Override
    public Cliente obtenerPorId(Long id) {
        return clienteRepositoryPort.buscarPorId(id).orElseThrow(() -> new RuntimeException("Cliente con enccontrado con el id: " + id));
    }

    @Override
    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        Cliente existente = obtenerPorId(id);
        existente.setTipoIdentificacion(clienteActualizado.getTipoIdentificacion());
        existente.setNumeroIdentificacion(clienteActualizado.getNumeroIdentificacion());
        existente.setNombres(clienteActualizado.getNombres());
        existente.setApellidos(clienteActualizado.getApellidos());
        existente.setCorreo(clienteActualizado.getCorreo());
        existente.setFechaNacimiento(clienteActualizado.getFechaNacimiento());
        existente.setFechaModificacion(LocalDateTime.now());

        return clienteRepositoryPort.guardar(existente);
    }

    @Override
    public void eliminar(Long id) {
        obtenerPorId(id);
        if (cuentaRepositoryPort.existenCuentasPorClienteId(id)) {
            throw new RuntimeException("No se puede eliminar el cliente porque tieene cuentas asociadas");
        }
        clienteRepositoryPort.eliminar(id);
    }
}
