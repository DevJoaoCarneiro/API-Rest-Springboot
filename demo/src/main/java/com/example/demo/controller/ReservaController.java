package com.example.demo.controller;

import com.example.demo.Entities.Reserva;
import com.example.demo.dto.ReservaDTO;
import com.example.demo.dto.ReservaResponseDTO;
import com.example.demo.service.ReservaService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reserva")
@AllArgsConstructor
public class ReservaController {

    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> cadastraReserva(@RequestBody ReservaDTO reservaDTO) {
        reservaService.cadastrarReserva(reservaDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.cadastrarReserva(reservaDTO));
    }

    @GetMapping
    public List<ReservaResponseDTO> consultaReserva() {
        return reservaService.consultaReserva();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> consultaReservaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reservaService.consultaReservaPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteReservaPorId(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> editaReserva(@PathVariable Long id, @RequestBody ReservaDTO reservaDTO){
        return ResponseEntity.ok().body(reservaService.editaReserva(id, reservaDTO));
    }
}
