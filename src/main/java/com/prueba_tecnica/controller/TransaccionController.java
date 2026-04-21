package com.prueba_tecnica.controller;

import com.prueba_tecnica.entity.Transaccion;
import com.prueba_tecnica.service.TransaccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

// Controlador REST para transacciones bancarias
@RestController
@RequestMapping("/transacciones")
public class TransaccionController {

    private final TransaccionService transaccionService;

    public TransaccionController(TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    // POST /transacciones/consignar?cuentaDestinoId=1&monto=50000
    @PostMapping("/consignar")
    public ResponseEntity<Transaccion> consignar(
            @RequestParam Long cuentaDestinoId,
            @RequestParam BigDecimal monto) {
        Transaccion transaccion = transaccionService.consignar(cuentaDestinoId, monto);
        return new ResponseEntity<>(transaccion, HttpStatus.CREATED);
    }

    // POST /transacciones/retirar?cuentaOrigenId=1&monto=20000
    @PostMapping("/retirar")
    public ResponseEntity<Transaccion> retirar(
            @RequestParam Long cuentaOrigenId,
            @RequestParam BigDecimal monto) {
        Transaccion transaccion = transaccionService.retirar(cuentaOrigenId, monto);
        return new ResponseEntity<>(transaccion, HttpStatus.CREATED);
    }

    // POST /transacciones/transferir?cuentaOrigenId=1&cuentaDestinoId=2&monto=10000
    @PostMapping("/transferir")
    public ResponseEntity<Transaccion> transferir(
            @RequestParam Long cuentaOrigenId,
            @RequestParam Long cuentaDestinoId,
            @RequestParam BigDecimal monto) {
        Transaccion transaccion = transaccionService.transferir(cuentaOrigenId, cuentaDestinoId, monto);
        return new ResponseEntity<>(transaccion, HttpStatus.CREATED);
    }

    // GET /transacciones — Listar todas las transacciones
    @GetMapping
    public ResponseEntity<List<Transaccion>> listarTodas() {
        return ResponseEntity.ok(transaccionService.listarTodas());
    }
}
