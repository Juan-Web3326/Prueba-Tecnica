package com.prueba_tecnica.service;

import com.prueba_tecnica.entity.Cliente;
import com.prueba_tecnica.repository.ClienteRepository;
import com.prueba_tecnica.repository.CuentaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

// Implementación del servicio de clientes
@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository, CuentaRepository cuentaRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
    }

    // Crear cliente — valida edad mínima 18 años
    @Override
    public Cliente crear(Cliente cliente) {
        // Validar edad mínima
        int edad = Period.between(cliente.getFechaNacimiento(), LocalDate.now()).getYears();
        if (edad < 18) {
            throw new RuntimeException("El cliente debe ser mayor de edad (mínimo 18 años)");
        }

        // Validar que no exista el número de identificación
        if (clienteRepository.existsByNumeroIdentificacion(cliente.getNumeroIdentificacion())) {
            throw new RuntimeException("Ya existe un cliente con ese número de identificación");
        }

        // Validar que no exista el correo
        if (clienteRepository.existsByCorreo(cliente.getCorreo())) {
            throw new RuntimeException("Ya existe un cliente con ese correo");
        }

        return clienteRepository.save(cliente);
    }

    // Listar todos los clientes
    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    // Obtener cliente por id
    @Override
    public Cliente obtenerPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
    }

    // Actualizar cliente — fecha_modificacion se actualiza automáticamente con @PreUpdate
    @Override
    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        Cliente clienteExistente = obtenerPorId(id);

        clienteExistente.setTipoIdentificacion(clienteActualizado.getTipoIdentificacion());
        clienteExistente.setNumeroIdentificacion(clienteActualizado.getNumeroIdentificacion());
        clienteExistente.setNombres(clienteActualizado.getNombres());
        clienteExistente.setApellidos(clienteActualizado.getApellidos());
        clienteExistente.setCorreo(clienteActualizado.getCorreo());
        clienteExistente.setFechaNacimiento(clienteActualizado.getFechaNacimiento());

        return clienteRepository.save(clienteExistente);
    }

    // Eliminar cliente — valida que no tenga cuentas asociadas
    @Override
    public void eliminar(Long id) {
        Cliente cliente = obtenerPorId(id);

        // Verificar si tiene cuentas asociadas
        if (cuentaRepository.existsByClienteId(id)) {
            throw new RuntimeException("No se puede eliminar el cliente porque tiene cuentas asociadas");
        }

        clienteRepository.delete(cliente);
    }
}
