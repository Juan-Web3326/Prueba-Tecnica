package com.prueba_tecnica;

import com.prueba_tecnica.entity.Cuenta;
import com.prueba_tecnica.entity.Cuenta.EstadoCuenta;
import com.prueba_tecnica.entity.Cuenta.TipoCuenta;
import com.prueba_tecnica.entity.Transaccion;
import com.prueba_tecnica.repository.CuentaRepository;
import com.prueba_tecnica.repository.TransaccionRepository;
import com.prueba_tecnica.service.TransaccionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceImplTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private TransaccionRepository transaccionRepository;

    @InjectMocks
    private TransaccionServiceImpl transaccionService;
    private Cuenta cuentaAhorros;
    private Cuenta cuentaCorriente;

    // Prepara dos cuentas de prueba antes de cada test
    @BeforeEach
    void setUp() {
        cuentaAhorros = new Cuenta();
        cuentaAhorros.setId(1L);
        cuentaAhorros.setTipoCuenta(TipoCuenta.AHORROS);
        cuentaAhorros.setEstado(EstadoCuenta.ACTIVA);
        cuentaAhorros.setSaldo(new BigDecimal("100000"));
        cuentaAhorros.setSaldoDisponible(new BigDecimal("100000"));
        cuentaCorriente = new Cuenta();
        cuentaCorriente.setId(2L);
        cuentaCorriente.setTipoCuenta(TipoCuenta.CORRIENTE);
        cuentaCorriente.setEstado(EstadoCuenta.ACTIVA);
        cuentaCorriente.setSaldo(new BigDecimal("50000"));
        cuentaCorriente.setSaldoDisponible(new BigDecimal("50000"));
    }
    //PRUEBA 1: Consignación exitosa — saldo debe aumentar
    @Test
    void consignar_MontoValido_DeberiaAumentarSaldo() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaAhorros));
        when(cuentaRepository.save(any())).thenReturn(cuentaAhorros);
        when(transaccionRepository.save(any())).thenReturn(new Transaccion());
        transaccionService.consignar(1L, new BigDecimal("20000"));

        // Verificamos que el saldo aumentó correctamente
        assertEquals(new BigDecimal("120000"), cuentaAhorros.getSaldo());
    }
    //PRUEBA 2: Retiro exitoso en cuenta de ahorros con saldo suficiente
    @Test
    void retirar_SaldoSuficiente_DeberiaReducirSaldo() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaAhorros));
        when(cuentaRepository.save(any())).thenReturn(cuentaAhorros);
        when(transaccionRepository.save(any())).thenReturn(new Transaccion());
        transaccionService.retirar(1L, new BigDecimal("30000"));

        // 100000 - 30000 = 70000
        assertEquals(new BigDecimal("70000"), cuentaAhorros.getSaldo());
    }
    // PRUEBA 3: Retiro en ahorros con saldo insuficiente → excepción
    @Test
    void retirar_SaldoInsuficienteEnAhorros_DeberiaLanzarExcepcion() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaAhorros));
        // Intentamos retirar más de lo que hay (100000)
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            transaccionService.retirar(1L, new BigDecimal("200000"));
        });
        assertTrue(excepcion.getMessage().contains("Saldo insuficiente"));
    }
    //PRUEBA 4: Cuenta corriente SÍ permite sobregiro
    @Test
    void retirar_SobreGiroEnCorriente_DeberiaPermitirse() {
        when(cuentaRepository.findById(2L)).thenReturn(Optional.of(cuentaCorriente));
        when(cuentaRepository.save(any())).thenReturn(cuentaCorriente);
        when(transaccionRepository.save(any())).thenReturn(new Transaccion());

        // Retiramos 80000 cuando solo hay 50000 (sobregiro de -30000)
        assertDoesNotThrow(() -> transaccionService.retirar(2L, new BigDecimal("80000")));
    }
    //PRUEBA 5: Transferir a la misma cuenta → excepción
    @Test
    void transferir_MismaCuenta_DeberiaLanzarExcepcion() {
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            transaccionService.transferir(1L, 1L, new BigDecimal("10000"));
        });
        assertEquals("No se puede transferir a la misma cuenta", excepcion.getMessage());
    }
    //PRUEBA 6: Monto negativo → excepción
    @Test
    void consignar_MontoNegativo_DeberiaLanzarExcepcion() {
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            transaccionService.consignar(1L, new BigDecimal("-5000"));
        });
        assertEquals("El monto debe ser mayor a $0", excepcion.getMessage());
    }
    //PRUEBA 7: Cuenta inactiva → excepción
    @Test
    void retirar_CuentaInactiva_DeberiaLanzarExcepcion() {
        cuentaAhorros.setEstado(EstadoCuenta.INACTIVA); // la desactivamos
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaAhorros));
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            transaccionService.retirar(1L, new BigDecimal("10000"));
        });
        assertTrue(excepcion.getMessage().contains("no está activa"));
    }
}
