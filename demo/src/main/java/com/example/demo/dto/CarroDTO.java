package com.example.demo.dto;

import java.math.BigDecimal;

public record CarroDTO(
        String modelo,
        String marca,
        Integer ano,
        String placa,
        BigDecimal precoDiaria
) {
}
