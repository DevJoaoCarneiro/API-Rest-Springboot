package com.example.demo.service;

import com.example.demo.Entities.Carro;
import com.example.demo.Entities.Reserva;
import com.example.demo.dto.CarroDTO;
import com.example.demo.dto.CarroResponseDTO;
import com.example.demo.dto.ReservaDTO;
import com.example.demo.mapper.CarroMapper;
import com.example.demo.repository.ICarroRepository;
import com.example.demo.repository.IReservaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CarroService {

    private ICarroRepository carroRepository;

    private IReservaRepository reservaRepository;

    private CarroMapper carroMapper;

    public CarroDTO cadastraNovoCarro(CarroDTO carroDto) {
        Carro novoCarro = carroMapper.toEntity(carroDto);
        novoCarro.setDisponivel(true);
        return carroMapper.toDTO(carroRepository.save(novoCarro));
    }

    public List<CarroResponseDTO> consultarTodosOsCarros() {

        List<Carro> carros = carroRepository.findAllByOrderByIdAsc();

        Map<Long, Reserva> reservasAtivasMap = reservaRepository.findByStatus(true)
                .stream()
                .collect(Collectors.toMap(reserva -> reserva.getCarro().getId(), reserva -> reserva));

        return carros
                .stream()
                .map(carro -> {
                    CarroDTO carroDTO = carroMapper.toDTO(carro);
                    Reserva reservaAtiva = reservasAtivasMap.get(carro.getId());
                    ReservaDTO reservaDTO = null;

                    if (reservaAtiva != null) {
                        reservaDTO = new ReservaDTO(
                                reservaAtiva.getCliente().getId(),
                                reservaAtiva.getCarro().getId(),
                                reservaAtiva.getDataFim()
                        );
                    }

                    return new CarroResponseDTO(carro.getId(), carroDTO, reservaDTO);

                }).collect(Collectors.toList());
    }

    public CarroResponseDTO consultaCarroPorId(Long id) {
        Carro carro = carroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carro com id " + id + "não encontrado"));
        Optional<Reserva> reservaAtiva = reservaRepository.findByCarroIdAndStatus(id, true);
        ReservaDTO reservaDTO = null;
        if (reservaAtiva.isPresent()) {
            Reserva reserva = reservaAtiva.get();
            reservaDTO = new ReservaDTO(
                    reserva.getCliente().getId(),
                    reserva.getCarro().getId(),
                    reserva.getDataFim()
            );
        }

        CarroDTO carroDTO = carroMapper.toDTO(carro);
        return new CarroResponseDTO(carro.getId(), carroDTO, reservaDTO);

    }

    public List<CarroDTO> filtraCarroPorReservas(){
        List<Carro> carro = carroRepository.findByDisponivelTrueOrderByIdAsc();

        if(carro.isEmpty()){
            throw new IllegalArgumentException("Não tem carros disponiveis para filtrar as reservas");
        }

        return carroMapper.toDTOList(carro);

    }

    public void deletaCarroPorId(Long id) {
        Carro carro = carroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carro com id " + id + "não encontrado"));

        if (!carro.getReservas().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir um cliente que possui reservas associadas.");
        }

        carroRepository.deleteById(id);
    }

    public CarroDTO editaCarroPorId(Long id, CarroDTO carroDto) {
        Carro carro = carroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carro com id " + id + "não encontrado"));

        if (!carro.getDisponivel()) {
            throw new IllegalStateException("Não é possível editar um carro que está reservado ou em manutenção.");
        }

        if (carroDto.modelo() != null) {
            carro.setModelo(carroDto.modelo());
        }
        if (carroDto.marca() != null) {
            carro.setMarca(carroDto.marca());
        }

        if (carroDto.ano() != null) {
            carro.setAno(carroDto.ano());
        }

        if (carroDto.placa() != null) {
            carro.setPlaca(carroDto.placa());
        }

        if (carroDto.precoDiaria() != null) {
            carro.setPrecoDiaria(carroDto.precoDiaria());
        }

        return carroMapper.toDTO(carroRepository.save(carro));
    }

}
