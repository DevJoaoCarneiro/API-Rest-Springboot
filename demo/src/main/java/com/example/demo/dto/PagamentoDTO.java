package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoDTO(BigDecimal valor, LocalDateTime dataPagamento, String formaPagamento, String status) {

}
