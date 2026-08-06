package com.prueba_tecnica.infrastructure.adapters.in.web;

import com.prueba_tecnica.domain.model.Cuenta;
import com.prueba_tecnica.domain.ports.in.CuentaUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaUseCase cuentaUseCase;

    public CuentaController(CuentaUseCase cuentaUseCase) {
        this.cuentaUseCase = cuentaUseCase;
    }

    @PostMapping
    public ResponseEntity<Cuenta> crear(@RequestBody Cuenta cuenta, @RequestParam Long clienteId) {
        return new ResponseEntity<>(cuentaUseCase.crear(cuenta, clienteId), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Cuenta>> listarTodas() {
        return ResponseEntity.ok(cuentaUseCase.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaUseCase.obtenerPorId(id));
    }

    @PatchMapping("/{id}/activar")
    public ResponseEntity<Cuenta> activar(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaUseCase.activar(id));
    }

    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<Cuenta> inactivar(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaUseCase.inactivar(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Cuenta> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaUseCase.cancelar(id));
    }
}
