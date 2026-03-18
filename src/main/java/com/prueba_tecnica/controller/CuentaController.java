package com.prueba_tecnica.controller;

import com.prueba_tecnica.entity.Cuenta;
import com.prueba_tecnica.service.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador REST para el módulo de cuentas (productos)
@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    // POST /cuentas?clienteId=1 — Crear cuenta vinculada a un cliente
    @PostMapping
    public ResponseEntity<Cuenta> crear(@RequestBody Cuenta cuenta, @RequestParam Long clienteId) {
        Cuenta nueva = cuentaService.crear(cuenta, clienteId);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // GET /cuentas — Listar todas las cuentas
    @GetMapping
    public ResponseEntity<List<Cuenta>> listarTodas() {
        return ResponseEntity.ok(cuentaService.listarTodas());
    }

    // GET /cuentas/{id} — Obtener cuenta por id
    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerPorId(id));
    }

    // PATCH /cuentas/{id}/activar — Activar cuenta
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Cuenta> activar(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.activar(id));
    }

    // PATCH /cuentas/{id}/inactivar — Inactivar cuenta
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<Cuenta> inactivar(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.inactivar(id));
    }

    // PATCH /cuentas/{id}/cancelar — Cancelar cuenta (solo si saldo = 0)
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Cuenta> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.cancelar(id));
    }
}
