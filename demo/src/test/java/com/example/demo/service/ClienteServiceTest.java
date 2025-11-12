package com.example.demo.service;

import com.example.demo.Entities.Cliente;
import com.example.demo.Entities.Reserva;
import com.example.demo.Entities.embedded.Endereco;
import com.example.demo.dto.ClienteDTO;
import com.example.demo.dto.ConsultaClienteDTO;
import com.example.demo.dto.PatchClienteDTO;
import com.example.demo.mapper.ClienteMapper;
import com.example.demo.repository.IClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private IClientRepository clientRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteService clienteService;

    @Captor
    private ArgumentCaptor<Cliente> clienteCaptor;

    private Cliente criarCliente() {
        var endereco = new Endereco("Rua A", "87000-000", "Centro", "10", "PR");
        return new Cliente(1L, "João", "joao@email.com", "4499999999", endereco, "12345678900", List.of());
    }

    private ClienteDTO criarClienteDTO() {
        var endereco = new Endereco("Rua A", "87000-000", "Centro", "10", "PR");
        return new ClienteDTO("João", "joao@email.com", "4499999999", endereco, "12345678900");
    }

    private ConsultaClienteDTO criarConsultaDTO() {
        var endereco = new Endereco("Rua A", "87000-000", "Centro", "10", "PR");
        return new ConsultaClienteDTO(1L, "João", "joao@email.com", "4499999999", endereco);
    }
    private Endereco criarEndereco() {
        return new Endereco(
                "Rua A",
                "87000-000",
                "Centro",
                "10",
                "PR"
        );
    }


    @Nested
    @DisplayName("Cadastrar cliente")
    class CadastrarCliente {

        @Test
        @DisplayName("Deve cadastrar um cliente com sucesso")
        void deveCadastrarClienteComSucesso() {

            // Arrange
            var clienteDTO = criarClienteDTO();
            var cliente = criarCliente();

            when(clienteMapper.toEntity(clienteDTO)).thenReturn(cliente);
            when(clientRepository.save(any())).thenReturn(cliente);
            when(clienteMapper.toDTO(cliente)).thenReturn(clienteDTO);

            // Act
            var result = clienteService.cadastraCliente(clienteDTO);

            // Assert
            assertNotNull(result);
            assertEquals(clienteDTO.nome(), result.nome());
            assertEquals(clienteDTO.email(), result.email());

            verify(clientRepository).save(clienteCaptor.capture());
            assertEquals("João", clienteCaptor.getValue().getNome());
        }

        @Test
        @DisplayName("Deve lançar exceção ao ocorrer erro no repositório")
        void deveLancarExcecaoAoOcorrerErroNoRepositorio() {
            var clienteDTO = criarClienteDTO();
            when(clienteMapper.toEntity(clienteDTO)).thenThrow(new RuntimeException("Erro ao mapear"));

            assertThrows(RuntimeException.class, () -> clienteService.cadastraCliente(clienteDTO));
        }
    }

    @Nested
    @DisplayName("Consultar clientes")
    class ConsultarCliente {

        @Test
        @DisplayName("Deve retornar lista de clientes com sucesso")
        void deveConsultarClientesComSucesso() {
            var cliente = criarCliente();
            var consultaDTO = criarConsultaDTO();

            when(clientRepository.findAll()).thenReturn(List.of(cliente));
            when(clienteMapper.toDTOList(anyList())).thenReturn(List.of(consultaDTO));

            var result = clienteService.consultaCliente();

            assertEquals(1, result.size());
            assertEquals("João", result.get(0).nome());
            verify(clientRepository).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não houver clientes")
        void deveRetornarListaVaziaQuandoNaoHouverClientes() {
            when(clientRepository.findAll()).thenReturn(List.of());
            when(clienteMapper.toDTOList(anyList())).thenReturn(List.of());

            var result = clienteService.consultaCliente();

            assertTrue(result.isEmpty());
            verify(clientRepository).findAll();
        }
    }


    @Nested
    @DisplayName("Editar cliente")
    class EditarCliente {

        @Test
        @DisplayName("Deve editar cliente com sucesso")
        void deveEditarClienteComSucesso() {

            var cliente = criarCliente();

            var atualizacao = new PatchClienteDTO(
                    "João Atualizado",
                    "joao_atualizado@email.com",
                    "44988887777",
                    criarEndereco()
            );

            when(clientRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(clientRepository.save(any())).thenReturn(cliente);

            var result = clienteService.editaCliente(1L, atualizacao);

            assertNotNull(result);
            assertEquals("João Atualizado", result.nome());
            assertEquals("joao_atualizado@email.com", result.email());
            assertEquals("44988887777", result.telefone());

            verify(clientRepository).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção ao editar cliente inexistente")
        void deveLancarExcecaoAoEditarClienteInexistente() {

            when(clientRepository.findById(99L)).thenReturn(Optional.empty());

            var dto = new PatchClienteDTO(null, null, null, null);

            assertThrows(EntityNotFoundException.class,
                    () -> clienteService.editaCliente(99L, dto));
        }
    }
    @Nested
    @DisplayName("Deletar cliente")
    class DeletarCliente {

        @Test
        @DisplayName("Deve deletar cliente com sucesso")
        void deveDeletarClienteComSucesso() {
            var cliente = criarCliente();
            cliente.setReservas(List.of());
            when(clientRepository.findById(1L)).thenReturn(Optional.of(cliente));

            var result = clienteService.deletaCliente(1L);

            assertEquals(ResponseEntity.ok().build(), result);
            verify(clientRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar deletar cliente com reservas")
        void deveLancarExcecaoAoTentarDeletarClienteComReservas() {
            var cliente = criarCliente();
            cliente.setReservas(List.of(mock(Reserva.class)));
            when(clientRepository.findById(1L)).thenReturn(Optional.of(cliente));

            assertThrows(IllegalStateException.class, () -> clienteService.deletaCliente(1L));
            verify(clientRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar deletar cliente inexistente")
        void deveLancarExcecaoAoDeletarClienteInexistente() {
            when(clientRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> clienteService.deletaCliente(1L));
            verify(clientRepository, never()).deleteById(any());
        }
    }


    @Nested
    @DisplayName("Buscar cliente por ID")
    class BuscarClientePorId {

        @Test
        @DisplayName("Deve buscar cliente com sucesso")
        void deveBuscarClienteComSucesso() {
            var cliente = criarCliente();
            when(clientRepository.findById(1L)).thenReturn(Optional.of(cliente));

            var result = clienteService.buscaClientePorId(1L);

            assertEquals(ResponseEntity.ok(cliente), result);
            verify(clientRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção ao buscar cliente inexistente")
        void deveLancarExcecaoAoBuscarClienteInexistente() {
            when(clientRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> clienteService.buscaClientePorId(1L));
        }
    }
}
