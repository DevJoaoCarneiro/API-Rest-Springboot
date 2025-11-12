package com.example.demo.service;

import com.example.demo.Entities.Carro;
import com.example.demo.Entities.Cliente;
import com.example.demo.Entities.Reserva;
import com.example.demo.dto.ReservaDTO;
import com.example.demo.dto.ReservaResponseDTO;
import com.example.demo.mapper.ReservaMapper;
import com.example.demo.repository.ICarroRepository;
import com.example.demo.repository.IClientRepository;
import com.example.demo.repository.IReservaRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReservaService {

    @Autowired
    private IReservaRepository reservaRepository;

    @Autowired
    private ICarroRepository carroRepository;

    @Autowired
    private IClientRepository clientRepository;

    private ReservaMapper reservaMapper;

    public ReservaResponseDTO cadastrarReserva(ReservaDTO reservaDTO) {
        Carro carro = carroRepository.findById(reservaDTO.carro_id())
                .orElseThrow(() -> new IllegalArgumentException("Id do carro não encontrado"));

        Cliente cliente = clientRepository.findById(reservaDTO.cliente_id())
                .orElseThrow(() -> new IllegalArgumentException("Id do cliente não encontrado"));

        if (!carro.getDisponivel()) {
            throw new IllegalStateException("O carro selecionado não está disponível.");
        }

        if (reservaDTO.dataFim().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data de fim não pode estar no passado.");
        }


        Reserva reserva = new Reserva();
        reserva.setDataInicio(LocalDateTime.now());
        reserva.setDataFim(reservaDTO.dataFim());
        reserva.setStatus(true);
        reserva.setCarro(carro);
        reserva.setCliente(cliente);
        carro.setDisponivel(false);

        reservaRepository.save(reserva);

        return reservaMapper.toDTO(reserva);
    }

    public List<ReservaResponseDTO> consultaReserva() {
        return reservaMapper.toListDTO(reservaRepository.findAll());
    }

    public ReservaResponseDTO consultaReservaPorId(Long id){
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Reserva não encontrada"));
        return reservaMapper.toDTO(reserva);
    }

    public ResponseEntity deletaReserva(Long id){
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Reserva não encontrada"));

        if(reserva.isStatus()){
            throw new IllegalArgumentException("Você não pode excluir uma reserva que está ativa");
        }

        if(reserva.getCarro().getDisponivel()){
            throw new IllegalArgumentException("Não é possível excluir uma reserva enquanto o carro não estiver disponível");
        }

        reservaRepository.delete(reserva);
        return ResponseEntity.ok("Reserva excluída com sucesso");
    }

    public ReservaResponseDTO atualizaReservaPorId(Long id){
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Reserva não encontrada"));


        if (reserva.isStatus()) {
            reserva.getCarro().setDisponivel(true);
            reserva.setStatus(false);
        } else {
            reserva.getCarro().setDisponivel(false);
            reserva.setStatus(true);
        }

        return reservaMapper.toDTO(reservaRepository.save(reserva));
    }
}


