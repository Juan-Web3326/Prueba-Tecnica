package com.prueba_tecnica.infrastructure.config;

import com.prueba_tecnica.application.service.ClienteService;
import com.prueba_tecnica.application.service.CuentaService;
import com.prueba_tecnica.application.service.TransaccionService;
import com.prueba_tecnica.domain.ports.in.ClienteUseCase;
import com.prueba_tecnica.domain.ports.in.CuentaUseCase;
import com.prueba_tecnica.domain.ports.in.TransaccionUseCase;
import com.prueba_tecnica.domain.ports.out.ClienteRepositoryPort;
import com.prueba_tecnica.domain.ports.out.CuentaRepositoryPort;
import com.prueba_tecnica.domain.ports.out.TransaccionRepositoryPort;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.ClientePersistenceAdapter;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.CuentaPersistenceAdapter;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.TransaccionPersistenceAdapter;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.repository.ClienteJpaRepository;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.repository.CuentaJpaRepository;
import com.prueba_tecnica.infrastructure.adapters.out.persistence.repository.TransaccionJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    // ── Adaptadores de Persistencia (implementan los puertos de salida) ───────

    @Bean
    public ClienteRepositoryPort clienteRepositoryPort(ClienteJpaRepository clienteJpaRepository) {
        return new ClientePersistenceAdapter(clienteJpaRepository);
    }

    @Bean
    public CuentaRepositoryPort cuentaRepositoryPort(CuentaJpaRepository cuentaJpaRepository) {
        return new CuentaPersistenceAdapter(cuentaJpaRepository);
    }

    @Bean
    public TransaccionRepositoryPort transaccionRepositoryPort(TransaccionJpaRepository transaccionJpaRepository) {
        return new TransaccionPersistenceAdapter(transaccionJpaRepository);
    }

    // ── Servicios de Aplicación (implementan los casos de uso) ────────────────

    @Bean
    public ClienteUseCase clienteUseCase(ClienteRepositoryPort clienteRepositoryPort, CuentaRepositoryPort cuentaRepositoryPort) {
        return new ClienteService(clienteRepositoryPort, cuentaRepositoryPort);
    }

    @Bean
    public CuentaUseCase cuentaUseCase(CuentaRepositoryPort cuentaRepositoryPort, ClienteRepositoryPort clienteRepositoryPort) {
        return new CuentaService(cuentaRepositoryPort, clienteRepositoryPort);
    }

    @Bean
    public TransaccionUseCase transaccionUseCase(TransaccionRepositoryPort transaccionRepositoryPort, CuentaRepositoryPort cuentaRepositoryPort) {
        return new TransaccionService(transaccionRepositoryPort, cuentaRepositoryPort);
    }
}
