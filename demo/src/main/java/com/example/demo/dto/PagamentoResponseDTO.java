package com.example.demo.dto;

import com.example.demo.Entities.embedded.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoResponseDTO(
        Long id,
        Status status,
        BigDecimal valor,
        LocalDateTime dataPagamento,
        String formaPagamento,
        String tipoPagamento,
        ReservaResponseDTO reserva
) {
}
