package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entities.Carro;

@Repository
public interface ICarroRepository extends JpaRepository<Carro, Long>{

}
