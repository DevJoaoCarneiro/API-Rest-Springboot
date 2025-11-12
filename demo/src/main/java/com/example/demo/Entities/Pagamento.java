package com.example.demo.Entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.Entities.embedded.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

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
