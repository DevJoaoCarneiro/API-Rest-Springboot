package com.example.demo.controller;


import com.example.demo.dto.ReservaDTO;
import com.example.demo.dto.ReservaResponseDTO;
import com.example.demo.service.ReservaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Reserva", description = "Endpoints para gerenciamento de reservas")
@RestController
@RequestMapping("/api/reserva")
@AllArgsConstructor
public class ReservaController {

    private ReservaService reservaService;

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> cadastraReserva(@RequestBody ReservaDTO reservaDTO) {

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
        reservaService.deletaReserva(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> atualizaStatusDaReserva(@PathVariable Long id){
        return ResponseEntity.ok().body(reservaService.atualizaReservaPorId(id));
    }
}
