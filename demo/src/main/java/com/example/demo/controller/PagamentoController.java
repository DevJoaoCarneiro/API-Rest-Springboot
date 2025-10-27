package com.example.demo.controller;

import java.util.List;

import com.example.demo.dto.PagamentoResponseDTO;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.PagamentoDTO;
import com.example.demo.repository.IPagamentoRepository;
import com.example.demo.service.PagamentoService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("api/pagamento")
@AllArgsConstructor
public class PagamentoController {

    private IPagamentoRepository pagamentoRepository;
    private PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> cadastroPagamento(@RequestBody @Valid PagamentoDTO pagamentoDTO) {
        PagamentoResponseDTO novoPagamento = pagamentoService.cadastroPagamento(pagamentoDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoPagamento);
    }


    @GetMapping
    public List<PagamentoResponseDTO> consultaPagamento() {
        return pagamentoService.consultarPagamento();
    }
    /*
    @DeleteMapping("/{id}")
    public ResponseEntity apagaPagamento(@PathVariable Long id) {
        return pagamentoService.deletaPagamento(id);

    }

    @PutMapping("/{id}")
    public EditaPagamentoDto editaPagamento(@PathVariable Long id, @RequestBody @Valid EditaPagamentoDto pagamentoDto){
        return pagamentoService.editaPagamento(id, pagamentoDto);
    }
    */

}
