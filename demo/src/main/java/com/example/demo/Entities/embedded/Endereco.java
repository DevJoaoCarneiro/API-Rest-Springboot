package com.example.demo.Entities.embedded;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Endereco {
    private String rua;
    private String cep;
    private String bairro;
    private String complemento;
    private String numero;

}
