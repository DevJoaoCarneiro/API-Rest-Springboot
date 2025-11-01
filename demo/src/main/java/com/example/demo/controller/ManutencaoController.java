package com.example.demo.controller;

import com.example.demo.dto.ManutencaoDTO;
import com.example.demo.dto.ManutencaoResponseDTO;
import com.example.demo.service.ManutencaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Manutenção", description = "Endpoints para gerenciamento de manutenções")
@RestController
@RequestMapping("/api/manutencao")
@AllArgsConstructor
public class ManutencaoController {

    private ManutencaoService manutencaoService;

    @PostMapping
    public ResponseEntity<ManutencaoResponseDTO> cadastraManutencao(@RequestBody ManutencaoDTO manutencaoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(manutencaoService.cadastraManutencao(manutencaoDTO));
    }

    @GetMapping
    public ResponseEntity<List<ManutencaoResponseDTO>> consultaTodasAsManutencoes() {
        return ResponseEntity.ok().body(manutencaoService.consultaTodasManutencoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManutencaoResponseDTO> consultaManutencaoPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(manutencaoService.consultaManutencaoPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletaPorIdManutencao(@PathVariable Long id) {
        manutencaoService.deletaManutencaoPorId(id);
        return ResponseEntity.ok("Manutenção deletada com sucesso!");

    }

    @PutMapping("/{id}")
    public ResponseEntity<ManutencaoResponseDTO> editaCliente(@PathVariable Long id, @RequestBody ManutencaoDTO manutencaoDTO) {
        return ResponseEntity.ok().body(manutencaoService.editaManutencaoPorId(id, manutencaoDTO));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ManutencaoResponseDTO> atualizaStatusPorId(@PathVariable Long id){
        return ResponseEntity.ok().body(manutencaoService.editaStatusDaManutencao(id));
    }
}
