package com.example.demo.dto;

import java.time.LocalDateTime;

public record ManutencaoDTO(
        Long carro_id,
        LocalDateTime dataFim,
        String descricao
) {
}
