package com.example.demo.mapper;

import com.example.demo.Entities.Pagamento;
import com.example.demo.dto.PagamentoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ReservaMapper.class})
public interface PagamentoMapper {

    @Mapping(source = "reserva", target = "reserva")
    PagamentoResponseDTO toDTO(Pagamento pagamento);

    List<PagamentoResponseDTO> toListDTO(List<Pagamento> pagamentos);
}
