package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
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
        pagamento.setDataPagamento(pagamentoDTO.dataPagamento());
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
    public ResponseEntity deletaPagamento(Long id){
        Optional<Pagamento> pagamento = pagamentoRepository.findById(id);

        if(pagamento.isPresent()){
            pagamentoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
