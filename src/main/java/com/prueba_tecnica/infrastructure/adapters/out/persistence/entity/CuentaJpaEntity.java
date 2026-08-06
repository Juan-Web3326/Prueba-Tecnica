package com.prueba_tecnica.infrastructure.adapters.out.persistence.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuentas")
public class CuentaJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private ClienteJpaEntity cliente;
    @Column(name = "tipo_cuenta")
    private String tipoCuenta;
    @Column(name = "numero_cuenta", unique = true)
    private String numeroCuenta;
    @Column(name = "estado")
    private String estado;
    @Column(name = "saldo")
    private BigDecimal saldo;
    @Column(name = "saldo_disponible")
    private BigDecimal saldoDisponible;
    @Column(name = "exenta_gmf")
    private Boolean exentaGmf;
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    public CuentaJpaEntity() {
    }

    // Getters y Setters...
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClienteJpaEntity getCliente() {
        return cliente;
    }

    public void setCliente(ClienteJpaEntity cliente) {
        this.cliente = cliente;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public Boolean getExentaGmf() {
        return exentaGmf;
    }

    public void setExentaGmf(Boolean exentaGmf) {
        this.exentaGmf = exentaGmf;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
