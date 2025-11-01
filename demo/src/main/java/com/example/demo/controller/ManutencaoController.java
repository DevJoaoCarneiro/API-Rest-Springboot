package com.example.demo.controller;

import com.example.demo.Entities.Manutencao;
import com.example.demo.service.ManutencaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manutencoes")
public class ManutencaoController {

    @Autowired
    private ManutencaoService manutencaoService;

    @PostMapping
    public ResponseEntity<Manutencao> registrarManutencao(@RequestBody Manutencao manutencao) {
        Manutencao novaManutencao = manutencaoService.salvar(manutencao);
        return ResponseEntity.ok(novaManutencao);
    }

    @GetMapping
    public ResponseEntity<List<Manutencao>> listarManutencoes() {
        List<Manutencao> manutencoes = manutencaoService.listarTodas();
        return ResponseEntity.ok(manutencoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Manutencao> buscarPorId(@PathVariable Long id) {
        return manutencaoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (manutencaoService.buscarPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        manutencaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
