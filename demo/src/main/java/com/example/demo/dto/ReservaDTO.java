package com.example.demo.dto;

import java.time.LocalDateTime;

public record ReservaDTO(
        Long cliente_id,
        Long carro_id,
        LocalDateTime dataFim
) {
}
