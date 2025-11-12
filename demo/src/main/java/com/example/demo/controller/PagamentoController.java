package com.example.demo.controller;

import java.util.List;

import com.example.demo.Entities.embedded.Status;
import com.example.demo.dto.EditaPagamentoDto;
import com.example.demo.dto.PagamentoResponseDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Pagamento", description = "Endpoints para gerenciamento de pagamentos")
@RestController
@RequestMapping("api/pagamento")
@AllArgsConstructor
public class PagamentoController {

    private IPagamentoRepository pagamentoRepository;
    private PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<PagamentoResponseDTO> cadastrarPagamento(@RequestBody @Valid PagamentoDTO pagamentoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoService.cadastroPagamento(pagamentoDTO));

    }


    @GetMapping
    public List<PagamentoResponseDTO> consultaPagamento() {
        return pagamentoService.consultarPagamento();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity apagaPagamento(@PathVariable Long id) {
        return pagamentoService.deletaPagamento(id);

    }

    @PutMapping("/{id}")
    public ResponseEntity<EditaPagamentoDto> editaPagamento(@PathVariable Long id, @RequestBody @Valid EditaPagamentoDto pagamentoDto){
        return ResponseEntity.ok().body(pagamentoService.editaPagamento(id, pagamentoDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PagamentoResponseDTO> atualizaStatusDePagamento(@PathVariable Long id, @RequestBody @Valid Status status){
        return ResponseEntity.ok().body(pagamentoService.atualizaStatusDoPagamento(id,status));

    }
}
