package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.PatchClienteDTO;
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

    public ClienteDTO cadastraCliente(ClienteDTO clienteDTO) {
        Cliente cliente = clientMapper.toEntity(clienteDTO);
        return clientMapper.toDTO(clientRepository.save(cliente));
    }

    public List<ConsultaClienteDTO> consultaCliente() {
        return clientMapper.toDTOList(clientRepository.findAll());
    }

    public ConsultaClienteDTO editaCliente(Long id, PatchClienteDTO clienteDTO) {
        Cliente clienteParaAtualizar = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o ID: " + id));

        if (clienteDTO.nome() != null) {
            clienteParaAtualizar.setNome(clienteDTO.nome());
        }
        if (clienteDTO.email() != null) {
            clienteParaAtualizar.setEmail(clienteDTO.email());
        }
        if (clienteDTO.endereco() != null) {
            clienteParaAtualizar.setEndereco(clienteDTO.endereco());
        }
        if (clienteDTO.telefone() != null) {
            clienteParaAtualizar.setTelefone(clienteDTO.telefone());
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

    public ResponseEntity<Void> deletaCliente(Long id) {
        Cliente user = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
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
