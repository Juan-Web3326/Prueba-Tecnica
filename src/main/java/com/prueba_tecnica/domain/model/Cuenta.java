package com.prueba_tecnica.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Cuenta {
    private Long id;
    private Cliente cliente;
    private String tipoCuenta; // "AHORROS" o "CORRIENTE"
    private String numeroCuenta;
    private String estado;     // "ACTIVA", "INACTIVA", "CANCELADA"
    private BigDecimal saldo;
    private BigDecimal saldoDisponible;
    private Boolean exentaGmf;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    public Cuenta() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
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
