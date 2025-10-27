package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoDTO(
        Long manutencao_id,
        Long reserva_id,
        BigDecimal valor,
        String formaPagamento
) {

}
