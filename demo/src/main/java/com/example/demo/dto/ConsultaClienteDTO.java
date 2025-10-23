package com.example.demo.dto;

import com.example.demo.Entities.embedded.Endereco;

public record ConsultaClienteDTO(Long id,String nome, String email, String telefone, Endereco endereco) {

}
