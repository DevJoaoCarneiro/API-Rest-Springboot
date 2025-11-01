package com.example.demo.dto;

import java.time.LocalDateTime;

public record ManutencaoResponseDTO(
        LocalDateTime dataInicio,
        LocalDateTime dataFim,
        boolean status,
        String descricao,
        CarroDTO carro
) {
}
