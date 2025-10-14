package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.example.demo.dto.EditaPagamentoDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.Pagamento;
import com.example.demo.dto.PagamentoDTO;
import com.example.demo.repository.IPagamentoRepository;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PagamentoService {

    private IPagamentoRepository pagamentoRepository;

    public PagamentoDTO cadastroUser(PagamentoDTO pagamentoDTO) {
        Pagamento pagamento = new Pagamento();
        pagamento.setValor(pagamentoDTO.valor());
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setFormaPagamento(pagamentoDTO.formaPagamento());
        pagamento.setStatus(pagamentoDTO.status());
        Pagamento novoPagamento = pagamentoRepository.save(pagamento);

        return new PagamentoDTO(
                novoPagamento.getValor(),
                novoPagamento.getDataPagamento(),
                novoPagamento.getFormaPagamento(),
                novoPagamento.getStatus()
        );
    }

    public List<PagamentoDTO> consultarPagamento() {
        List<Pagamento> listaPagamento = pagamentoRepository.findAll();

        return listaPagamento
                .stream()
                .map(c -> new PagamentoDTO(c.getValor(), c.getDataPagamento(), c.getFormaPagamento(), c.getStatus()))
                .collect(Collectors.toList());
    }

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
}
