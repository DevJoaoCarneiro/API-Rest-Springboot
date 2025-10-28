package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.Carro;

import java.util.List;

@Repository
public interface ICarroRepository extends JpaRepository<Carro, Long>{
    List<Carro> findAllByOrderByIdAsc();
}
