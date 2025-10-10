package com.example.demo.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@Table(name = "carro")
@NoArgsConstructor
@AllArgsConstructor
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "modelo", nullable = false)
    private String modelo;

    @Column(name = "marca", nullable = false)
    private String marca;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "placa", nullable = false, unique = true)
    private String placa;

    @Column(name = "disponivel", nullable = false)
    private Boolean disponivel;

    @Column(name = "precoDiaria", nullable = false)
    private BigDecimal precoDiaria;

    @OneToMany(mappedBy = "carro")
    private List<Reserva> reservas;

    @OneToMany(mappedBy = "carro")
    private List<Manutencao> manutencoes;

}
