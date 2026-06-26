package com.prueba_tecnica;

import com.prueba_tecnica.entity.Cliente;
import com.prueba_tecnica.repository.ClienteRepository;
import com.prueba_tecnica.repository.CuentaRepository;
import com.prueba_tecnica.service.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// Le dice a JUnit que use Mockito para crear los mocks
@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    // Simula el repositorio sin tocar la base de datos real
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    // Inyecta los mocks en el servicio real que vamos a probar
    @InjectMocks
    private ClienteServiceImpl clienteService;
    private Cliente clienteValido;

    // Se ejecuta antes de cada prueba — prepara un cliente de ejemplo
    @BeforeEach
    void setUp() {
        clienteValido = new Cliente();
        clienteValido.setNombres("Itsuki");
        clienteValido.setApellidos("Nakano");
        clienteValido.setCorreo("itsuki@email.com");
        clienteValido.setNumeroIdentificacion("1235469987");
        clienteValido.setFechaNacimiento(LocalDate.of(2004,3,24));
    }
    //PRUEBA 1: Crear un cliente válido debería funcionar correctamente
    @Test
    void crear_ClienteValido_DeberiaGuardarlo(){
        // GIVEN (dado que): el número de ID y el correo NO existen aún en la BD
        when(clienteRepository.existsByNumeroIdentificacion("1235469987")).thenReturn(false);
        when(clienteRepository.existsByCorreo("itsuki@email.com")).thenReturn(false);
        when(clienteRepository.save(clienteValido)).thenReturn(clienteValido);

        // WHEN (cuando): llamamos al método crear
        Cliente resultado = clienteService.crear(clienteValido);

        // THEN (entonces): el resultado no debe ser null y se llamó al save exactamente 1 vez
        assertNotNull(resultado);
        verify(clienteRepository, times(1)).save(clienteValido);
    }
    //PRUEBA 2: Cliente menor de edad → debe lanzar excepción
    @Test
    void crear_ClienteMenorDeEdad_DeberiaLanzarExcepcion() {
        // GIVEN: cliente con fecha que lo hace menor de 18 años
        clienteValido.setFechaNacimiento(LocalDate.of(2015, 2, 25)); // 10 años

        // WHEN / THEN: esperamos que lance RuntimeException con ese mensaje
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            clienteService.crear(clienteValido);
        });
        assertEquals("El cliente debe ser mayor de edad (mínimo 18 años)", excepcion.getMessage());
    }
    //PRUEBA 3: Número de identificación duplicado → debe lanzar excepción
    @Test
    void crear_IdentificacionDuplicada_DeberiaLanzarExcepcion() {

        // GIVEN: ya existe ese número de identificación en la BD
        when(clienteRepository.existsByNumeroIdentificacion("1235469987")).thenReturn(true);

        // WHEN / THEN
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            clienteService.crear(clienteValido);
        });
        assertEquals("Ya existe un cliente con ese número de identificación", excepcion.getMessage());
    }
    //PRUEBA 4: Correo duplicado → debe lanzar excepción
    @Test
    void crear_CorreoDuplicado_DeberiaLanzarExcepcion() {

        // GIVEN: el número de ID es único pero el correo ya existe
        when(clienteRepository.existsByNumeroIdentificacion("1235469987")).thenReturn(false);
        when(clienteRepository.existsByCorreo("itsuki@email.com")).thenReturn(true);

        // WHEN / THEN
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            clienteService.crear(clienteValido);
        });
        assertEquals("Ya existe un cliente con ese correo", excepcion.getMessage());
    }
    //PRUEBA 5: Eliminar cliente con cuentas → debe lanzar excepción
    @Test
    void eliminar_ClienteConCuentas_DeberiaLanzarExcepcion() {

        // GIVEN: el cliente existe y tiene cuentas asociadas
        when(clienteRepository.findById(1L)).thenReturn(java.util.Optional.of(clienteValido));
        when(cuentaRepository.existsByClienteId(1L)).thenReturn(true);

        // WHEN / THEN
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            clienteService.eliminar(1L);
        });
        assertEquals("No se puede eliminar el cliente porque tiene cuentas asociadas", excepcion.getMessage());
    }
}