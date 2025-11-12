package com.example.demo.dto;

public record CarroResponseDTO(
        Long id,
        CarroDTO carro,
        ReservaDTO reserva
) {
}
