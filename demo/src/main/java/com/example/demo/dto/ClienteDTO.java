package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteDTO(@NotBlank String nome, @NotBlank String email, @NotBlank String telefone, String endereco, @NotBlank String documento) {

}
