package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoResponseDTO(
        Long id,
        boolean status,
        BigDecimal valor,
        LocalDateTime dataPagamento,
        String formaPagamento,
        String tipoPagamento,
        ReservaResponseDTO reserva
) {
}
