package com.example.demo.service;

import com.example.demo.Entities.Carro;
import com.example.demo.Entities.Cliente;
import com.example.demo.Entities.Reserva;
import com.example.demo.Entities.embedded.Endereco;
import com.example.demo.dto.CarroDTO;
import com.example.demo.dto.ClienteDTO;
import com.example.demo.dto.ReservaDTO;
import com.example.demo.dto.ReservaResponseDTO;
import com.example.demo.repository.ICarroRepository;
import com.example.demo.repository.IClientRepository;
import com.example.demo.repository.IReservaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservaService {

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private ICarroRepository carroRepository;

    @Autowired
    private IClientRepository clientRepository;

    public ReservaResponseDTO cadastrarReserva(ReservaDTO reservaDTO) {
        Carro carro = carroRepository.findById(reservaDTO.carro_id())
                .orElseThrow(() -> new IllegalArgumentException("Id do carro não encontrado"));

        Cliente cliente = clientRepository.findById(reservaDTO.cliente_id())
                .orElseThrow(() -> new IllegalArgumentException("Id do cliente não encontrado"));


        Reserva reserva = new Reserva();
        reserva.setDataInicio(LocalDateTime.now());
        reserva.setDataFim(reservaDTO.dataFim());
        reserva.setStatus(true);
        reserva.setCarro(carro);
        reserva.setCliente(cliente);

        reservaRepository.save(reserva);

        return new ReservaResponseDTO(
                reserva.getDataInicio(),
                reserva.getDataFim(),
                reserva.isStatus(),
                new ClienteDTO(
                        cliente.getNome(),
                        cliente.getEmail(),
                        cliente.getTelefone(),
                        cliente.getEndereco(),
                        cliente.getDocumento()
                ),
                new CarroDTO(
                        carro.getModelo(),
                        carro.getMarca(),
                        carro.getAno(),
                        carro.getPlaca(),
                        carro.getPrecoDiaria()
                )
        );

    }

    public List<ReservaResponseDTO> consultaReserva() {
        List<Reserva> reserva = reservaRepository.findAll();

        return reserva
                .stream()
                .map(c -> new ReservaResponseDTO(
                        c.getDataInicio(),
                        c.getDataFim(),
                        c.isStatus(),
                        new ClienteDTO(
                                c.getCliente().getNome(),
                                c.getCliente().getEmail(),
                                c.getCliente().getTelefone(),
                                c.getCliente().getEndereco(),
                                c.getCliente().getDocumento()
                        ),
                        new CarroDTO(
                                c.getCarro().getModelo(),
                                c.getCarro().getMarca(),
                                c.getCarro().getAno(),
                                c.getCarro().getPlaca(),
                                c.getCarro().getPrecoDiaria()
                        )
                ))
                .collect(Collectors.toList());
    }
}


