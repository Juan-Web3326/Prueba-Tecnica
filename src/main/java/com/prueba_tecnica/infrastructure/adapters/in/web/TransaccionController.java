package com.prueba_tecnica.infrastructure.adapters.in.web;

import com.prueba_tecnica.domain.model.Transaccion;
import com.prueba_tecnica.domain.ports.in.TransaccionUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/transacciones")
@CrossOrigin("*")
public class TransaccionController {

    private final TransaccionUseCase transaccionUseCase;

    public TransaccionController(TransaccionUseCase transaccionUseCase) {
        this.transaccionUseCase = transaccionUseCase;
    }

    @PostMapping("/consignar")
    public ResponseEntity<Transaccion> consignar(@RequestParam Long cuentaDestinoId, @RequestParam BigDecimal monto) {
        return new ResponseEntity<>(transaccionUseCase.consignar(cuentaDestinoId, monto), HttpStatus.CREATED);
    }

    @PostMapping("/retirar")
    public ResponseEntity<Transaccion> retirar(@RequestParam Long cuentaOrigenId, @RequestParam BigDecimal monto) {
        return new ResponseEntity<>(transaccionUseCase.retirar(cuentaOrigenId, monto), HttpStatus.CREATED);
    }

    @PostMapping("/transferir")
    public ResponseEntity<Transaccion> transferir(@RequestParam Long cuentaOrigenId, @RequestParam Long cuentaDestinoId, @RequestParam BigDecimal monto) {
        return new ResponseEntity<>(transaccionUseCase.transferir(cuentaOrigenId, cuentaDestinoId, monto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Transaccion>> listarTodas() {
        return ResponseEntity.ok(transaccionUseCase.listarTodas());
    }
}