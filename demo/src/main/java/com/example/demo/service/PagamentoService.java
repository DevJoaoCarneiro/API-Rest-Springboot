package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.Entities.*;
import com.example.demo.dto.PagamentoResponseDTO;
import com.example.demo.mapper.PagamentoMapper;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.PagamentoDTO;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PagamentoService {

    private IPagamentoRepository pagamentoRepository;

    @Autowired
    private IManutencaoRepository manutencaoRepository;

    @Autowired
    private IReservaRepository reservaRepository;

    private PagamentoMapper pagamentoMapper;

    public PagamentoResponseDTO cadastroPagamento(PagamentoDTO pagamentoDTO) {

        boolean temReserva = pagamentoDTO.reserva_id() != null;
        boolean temManutencao = pagamentoDTO.manutencao_id() != null;

        if (temReserva && temManutencao) {
            throw new IllegalArgumentException("Informe apenas uma referência: reserva OU manutenção.");
        }

        if (!temReserva && !temManutencao) {
            throw new IllegalArgumentException("É necessário informar uma reserva ou manutenção.");
        }


        Pagamento pagamento = new Pagamento();
        pagamento.setValor(pagamentoDTO.valor());
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setFormaPagamento(pagamentoDTO.formaPagamento());
        pagamento.setStatus(true);

        if (temReserva) {
            Reserva reserva = reservaRepository.findById(pagamentoDTO.reserva_id())
                    .orElseThrow(() -> new IllegalArgumentException("Reserva não encontrada"));
            pagamento.setTipoPagamento("Reserva");
            pagamento.setReserva(reserva);
        } else {
            Manutencao manutencao = manutencaoRepository.findById(pagamentoDTO.manutencao_id())
                    .orElseThrow(() -> new IllegalArgumentException("Manutenção não encontrada"));
            pagamento.setTipoPagamento("Manutencao");
            pagamento.setManutencao(manutencao);
        }

        return pagamentoMapper.toDTO(pagamentoRepository.save(pagamento));
    }


    public List<PagamentoResponseDTO> consultarPagamento() {
        return pagamentoMapper.toListDTO(pagamentoRepository.findAll());
    }
/*
    @Transactional
    public ResponseEntity deletaPagamento(Long id) {
        Optional<Pagamento> pagamento = pagamentoRepository.findById(id);

        if (!pagamentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Pagamento não encontrado com o ID: " + id);
        }

        pagamentoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public EditaPagamentoDto editaPagamento(Long id, EditaPagamentoDto editaPagamentoDTO) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado com o ID: " + id));


        if (editaPagamentoDTO.valor() != null) {
            pagamento.setValor(editaPagamentoDTO.valor());
        }
        if (editaPagamentoDTO.formaPagamento() != null) {
            pagamento.setFormaPagamento(editaPagamentoDTO.formaPagamento());
        }

        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

        return new EditaPagamentoDto(
                pagamentoSalvo.getValor(),
                pagamentoSalvo.getFormaPagamento()
        );

    }

     */
}
