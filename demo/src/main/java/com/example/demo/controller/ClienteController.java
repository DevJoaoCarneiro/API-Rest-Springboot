package com.example.demo.controller;

import java.util.List;

import com.example.demo.dto.PatchClienteDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.Entities.Cliente;
import com.example.demo.dto.ClienteDTO;
import com.example.demo.dto.ConsultaClienteDTO;
import com.example.demo.repository.IClientRepository;
import com.example.demo.service.ClienteService;

import lombok.AllArgsConstructor;

@Tag(name = "Cliente", description = "Endpoints para gerenciamento de clientes")
@RestController
@RequestMapping("/api/cliente")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class ClienteController {

    private ClienteService clientService;
    private IClientRepository clientRepository;


    @PostMapping
    public ResponseEntity<ClienteDTO> cadastraCliente(@RequestBody @Valid ClienteDTO clienteDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.cadastraCliente(clienteDto));
    }

    @GetMapping
    public List<ConsultaClienteDTO> consultaCliente() {
        return clientService.consultaCliente();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> consultaClientePorId(@PathVariable Long id) {
        return clientService.buscaClientePorId(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ConsultaClienteDTO> atualizaParcialmente(
            @PathVariable Long id,
            @RequestBody PatchClienteDTO patchDTO
    ) {
        ConsultaClienteDTO clienteAtualizado = clientService.editaCliente(id, patchDTO);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaCliente(@PathVariable Long id) {
        clientService.deletaCliente(id);
        return ResponseEntity.noContent().build();
    }
}
