package com.example.demo.Entities;


import java.util.List;

import com.example.demo.Entities.embedded.Endereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "cliente")
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Embedded
    private Endereco endereco;

    @Column(name = "documento", nullable = false, unique = true)
    private String documento;

    @OneToMany(mappedBy = "cliente")
    private List<Reserva> reservas;

}
