package com.example.demo.dto;

import java.time.LocalDateTime;

public record ReservaResponseDTO(
    LocalDateTime dataInicio,
    LocalDateTime dataFim,
    boolean status,
    ClienteDTO cliente,
    CarroDTO carro
) {
}
