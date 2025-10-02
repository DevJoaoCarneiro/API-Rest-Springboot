package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.Pagamento;

@Repository
public interface IPagamentoRepository extends JpaRepository<Pagamento, Long> {

}
