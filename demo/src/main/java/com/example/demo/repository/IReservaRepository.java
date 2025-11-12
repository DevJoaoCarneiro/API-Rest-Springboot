package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.Reserva;

import java.util.List;
import java.util.Optional;

@Repository
public interface IReservaRepository extends JpaRepository<Reserva, Long>{
    Optional<Reserva> findByCarroIdAndStatus(Long carroId, boolean status);

    List<Reserva> findByStatus(boolean status);
    
}
