package com.prueba_tecnica.infrastructure.adapters.out.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
public class TransaccionJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tipo_transaccion")
    private String tipoTransaccion;
    @ManyToOne
    @JoinColumn(name = "cuenta_origen_id")
    private CuentaJpaEntity cuentaOrigen;
    @ManyToOne
    @JoinColumn(name = "cuenta_destino_id")
    private CuentaJpaEntity cuentaDestino;
    @Column(name = "monto")
    private BigDecimal monto;
    @Column(name = "fecha", updatable = false)
    private LocalDateTime fecha;

    public TransaccionJpaEntity() {
    }

    // Getters y Setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public CuentaJpaEntity getCuentaOrigen() {
        return cuentaOrigen;
    }

    public void setCuentaOrigen(CuentaJpaEntity cuentaOrigen) {
        this.cuentaOrigen = cuentaOrigen;
    }

    public CuentaJpaEntity getCuentaDestino() {
        return cuentaDestino;
    }

    public void setCuentaDestino(CuentaJpaEntity cuentaDestino) {
        this.cuentaDestino = cuentaDestino;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}

