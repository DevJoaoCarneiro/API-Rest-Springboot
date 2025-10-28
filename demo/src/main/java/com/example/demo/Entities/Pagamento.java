package com.example.demo.Entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pagamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "dataPagamento", nullable = false)
    private LocalDateTime dataPagamento;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "formaPagamento", nullable = false)
    private String formaPagamento;

    @Column(name = "tipoPagamento", nullable = false)
    private String tipoPagamento;

    @OneToOne
    @JoinColumn(name = "manutencao_id")
    private Manutencao manutencao;

    @OneToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;


}
