package com.example.demo.mapper;

import com.example.demo.Entities.Carro;
import com.example.demo.dto.CarroDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CarroMapper {

    CarroDTO toDTO(Carro carro);

    Carro toEntity(CarroDTO carroDTO);

    List<CarroDTO> toDTOList(List<Carro> carro);
}
