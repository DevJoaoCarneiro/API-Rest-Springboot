package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entities.Cliente;
import com.example.demo.Entities.Usuario;
import com.example.demo.dto.ClienteDTO;
import com.example.demo.dto.ConsultaClienteDTO;
import com.example.demo.repository.IClientRepository;
import com.example.demo.service.ClienteService;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/cliente")
@AllArgsConstructor
public class ClienteController {

    private ClienteService clientService;
    private IClientRepository clientRepository;

  

    @PostMapping
    public ResponseEntity<Cliente> cadastraCliente(@RequestBody @Valid ClienteDTO clienteDto) {
        var newClient = clientService.cadastraCliente(clienteDto);
        return ResponseEntity.ok(newClient);
    }

    @GetMapping
    public List<ConsultaClienteDTO> consultaCliente() {
         return clientService.consultaCliente();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> consultaClientePorId(@PathVariable Long id){
        return clientService.buscaClientePorId(id);  
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaClienteDTO> editaCliente(@PathVariable Long id, @RequestBody @Valid ConsultaClienteDTO consultaDto){
        ConsultaClienteDTO clienteAtualizado = clientService.editaCliente(id, consultaDto);
         return ResponseEntity.ok(clienteAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cliente> deletaCliente(@PathVariable Long id) {
        clientService.deletaCliente(id);
        return ResponseEntity.noContent().build();
    }
}
