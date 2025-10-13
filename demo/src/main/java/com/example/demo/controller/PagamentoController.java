package com.example.demo.controller;

import java.util.List;

import com.example.demo.dto.EditaPagamentoDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entities.Pagamento;
import com.example.demo.dto.PagamentoDTO;
import com.example.demo.repository.IPagamentoRepository;
import com.example.demo.service.PagamentoService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("pagamento")
@AllArgsConstructor
public class PagamentoController {

    private IPagamentoRepository pagamentoRepository;
    private PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<PagamentoDTO> cadastroPagamento(@RequestBody PagamentoDTO pagamentoDTO) {
        PagamentoDTO novoPagamento = pagamentoService.cadastroUser(pagamentoDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoPagamento);
    }

    @GetMapping
    public List<PagamentoDTO> consultaPagamento() {
        return pagamentoService.consultarPagamento();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity apagaPagamento(@PathVariable Long id) {
        return pagamentoService.deletaPagamento(id);

    }

    @PutMapping("/{id}")
    public EditaPagamentoDto editaPagamento(@PathVariable Long id, @RequestBody EditaPagamentoDto pagamentoDto){
        return pagamentoService.editaPagamento(id, pagamentoDto);
    }


}
