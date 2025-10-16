package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.Cliente;
import com.example.demo.dto.ClienteDTO;
import com.example.demo.dto.ConsultaClienteDTO;
import com.example.demo.repository.IClientRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClienteService {
    private IClientRepository clientRepository;

    public Cliente cadastraCliente(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();

        cliente.setNome(clienteDTO.nome());
        cliente.setEmail(clienteDTO.email());
        cliente.setTelefone(clienteDTO.telefone());
        cliente.setEndereco(clienteDTO.endereco());
        cliente.setDocumento(clienteDTO.documento());

        return clientRepository.save(cliente);
    }

    public List<ConsultaClienteDTO> consultaCliente() {
        List<Cliente> listaCliente = clientRepository.findAll();
        return listaCliente
                .stream()
                .map(c -> new ConsultaClienteDTO(c.getNome(), c.getEmail(), c.getTelefone(), c.getEndereco()))
                .collect(Collectors.toList());

    }

    public ConsultaClienteDTO editaCliente(Long id, ConsultaClienteDTO consultaDto) {
        Cliente clienteParaAtualizar = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o ID: " + id));

        if (consultaDto.nome() != null) {
            clienteParaAtualizar.setNome(consultaDto.nome());
        }
        if (consultaDto.email() != null) {
            clienteParaAtualizar.setEmail(consultaDto.email());
        }
        if (consultaDto.endereco() != null) {
            clienteParaAtualizar.setEndereco(consultaDto.endereco());
        }
        if (consultaDto.telefone() != null) {
            clienteParaAtualizar.setTelefone(consultaDto.telefone());
        }

        Cliente clienteSalvo = clientRepository.save(clienteParaAtualizar);

        return new ConsultaClienteDTO(
                clienteSalvo.getNome(),
                clienteSalvo.getEmail(),
                clienteSalvo.getTelefone(),
                clienteSalvo.getEndereco());
    }

    public ResponseEntity<Cliente> deletaCliente(Long id) {
        var user = clientRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        clientRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Cliente> buscaClientePorId(Long id) {
        Cliente cliente = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));

        return ResponseEntity.ok(cliente);
    }
}
