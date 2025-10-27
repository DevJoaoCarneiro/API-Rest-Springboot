package com.example.demo.service;

import com.example.demo.Entities.Carro;
import com.example.demo.dto.CarroDTO;
import com.example.demo.mapper.CarroMapper;
import com.example.demo.repository.ICarroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarroService {

    private ICarroRepository carroRepository;

    private CarroMapper carroMapper;

    public CarroDTO cadastraNovoCarro(CarroDTO carroDto) {
        Carro novoCarro = carroMapper.toEntity(carroDto);
        novoCarro.setDisponivel(true);
        return carroMapper.toDTO(carroRepository.save(novoCarro));
    }

    public List<CarroDTO> consultarTodosOsCarros() {
        return carroMapper.toDTOList(carroRepository.findAll());
    }

    public CarroDTO consultaCarroPorId(Long id) {
        Carro carro = carroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carro com id " + id + "não encontrado"));
        return carroMapper.toDTO(carro);
    }

    public void deletaCarroPorId(Long id) {
        Carro carro = carroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carro com id " + id + "não encontrado"));

        carroRepository.deleteById(id);
    }

    public CarroDTO editaCarroPorId(Long id, CarroDTO carroDto) {
        Carro carro = carroRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carro com id " + id + "não encontrado"));

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
