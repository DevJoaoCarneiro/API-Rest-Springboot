package com.example.demo.Entities;
    

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "reserva")
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "ClienteId", nullable = false)
    private Long clienteId;

     @GeneratedValue(strategy = GenerationType.IDENTITY)
      @Column(name = "CarroId", nullable = false)
    private Long carroId;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Column(name = "Status",nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "carro_id")
    private Carro carro;

}
