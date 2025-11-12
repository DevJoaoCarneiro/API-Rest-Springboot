package com.example.demo.dto;

import com.example.demo.Entities.embedded.Endereco;

public record PatchClienteDTO(
        String nome,
        String email,
        String telefone,
        Endereco endereco
) {}
