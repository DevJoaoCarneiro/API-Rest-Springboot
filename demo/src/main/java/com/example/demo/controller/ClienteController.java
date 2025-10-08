package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entities.Cliente;
import com.example.demo.dto.ClienteDTO;
import com.example.demo.service.ClienteService;

import org.springframework.web.bind.annotation.RequestBody;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("cliente")
@AllArgsConstructor
public class ClienteController {

    private ClienteService clientService;

    @PostMapping
    public ResponseEntity<Cliente> cadastraCliente(@RequestBody ClienteDTO clienteDto){
        var newClient = clientService.cadastraCliente(clienteDto);
        return ResponseEntity.ok(newClient);
    }

}
