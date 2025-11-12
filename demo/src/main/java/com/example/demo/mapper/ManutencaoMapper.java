package com.example.demo.mapper;

import com.example.demo.Entities.Manutencao;
import com.example.demo.dto.ManutencaoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ManutencaoMapper {

    @Mapping(source = "carro", target = "carro")
    ManutencaoResponseDTO toDTO(Manutencao manutencao);

    List<ManutencaoResponseDTO> toListDTO(List<Manutencao> manutencoes);
}
