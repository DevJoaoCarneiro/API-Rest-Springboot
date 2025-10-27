package com.example.demo.mapper;

import com.example.demo.Entities.Reserva;
import com.example.demo.dto.ReservaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    @Mapping(source = "cliente", target = "clienteDTO")
    @Mapping(source = "carro", target = "carroDTO")
    ReservaResponseDTO toDTO(Reserva reserva);

    List<ReservaResponseDTO> toListDTO(List<Reserva> reservas);
}