package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.demo.mapper.ClienteMapper;
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

    private ClienteMapper clientMapper;

    public Cliente cadastraCliente(ClienteDTO clienteDTO) {
        Cliente cliente = clientMapper.toEntity(clienteDTO);
        return clientRepository.save(cliente);
    }

    public List<ConsultaClienteDTO> consultaCliente() {
        return clientMapper.toDTOList(clientRepository.findAll());
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
                clienteSalvo.getId(),
                clienteSalvo.getNome(),
                clienteSalvo.getEmail(),
                clienteSalvo.getTelefone(),
                clienteSalvo.getEndereco()
                );
    }

    public ResponseEntity<Cliente> deletaCliente(Long id) {
        Cliente user = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        if (!user.getReservas().isEmpty()) {
            throw new IllegalStateException("Não é possível excluir um cliente que possui reservas associadas.");
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
