package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteDTO(@NotBlank @Size(min=3, message = "O nome deve ter no minimo 3 caracteres") String nome,
                         @NotBlank @Email (message = "O formato do email é invalido") String email,
                         @NotBlank String telefone,
                         String endereco,
                         @NotBlank String documento) {

}
