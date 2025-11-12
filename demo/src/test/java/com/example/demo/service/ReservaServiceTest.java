package com.example.demo.service;

import com.example.demo.Entities.Carro;
import com.example.demo.Entities.Cliente;
import com.example.demo.Entities.Reserva;
import com.example.demo.dto.ReservaDTO;
import com.example.demo.dto.ReservaResponseDTO;
import com.example.demo.mapper.ReservaMapper;
import com.example.demo.repository.ICarroRepository;
import com.example.demo.repository.IClientRepository;
import com.example.demo.repository.IReservaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private IReservaRepository reservaRepository;

    @Mock
    private ICarroRepository carroRepository;

    @Mock
    private IClientRepository clientRepository;

    @Mock
    private ReservaMapper reservaMapper;

    @InjectMocks
    private ReservaService reservaService;

    @Captor
    private ArgumentCaptor<Reserva> reservaCaptor;

    private Carro criarCarroDisponivel() {
        var carro = new Carro();
        carro.setId(1L);
        carro.setModelo("Civic");
        carro.setMarca("Honda");
        carro.setDisponivel(true);
        return carro;
    }

    private Cliente criarCliente() {
        var cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João");
        return cliente;
    }

    private Reserva criarReserva() {
        var carro = criarCarroDisponivel();
        var cliente = criarCliente();
        var reserva = new Reserva();
        reserva.setId(1L);
        reserva.setDataInicio(LocalDateTime.now());
        reserva.setDataFim(LocalDateTime.now().plusDays(2));
        reserva.setStatus(true);
        reserva.setCarro(carro);
        reserva.setCliente(cliente);
        return reserva;
    }


    @Nested
    @DisplayName("Cadastrar reserva")
    class CadastrarReserva {

        @Test
        @DisplayName("Deve cadastrar reserva com sucesso")
        void deveCadastrarReservaComSucesso() {
            var carro = criarCarroDisponivel();
            var cliente = criarCliente();
            var dto = new ReservaDTO(1L, 1L, LocalDateTime.now().plusDays(3));
            var reserva = criarReserva();
            var response = mock(ReservaResponseDTO.class);

            when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));
            when(clientRepository.findById(1L)).thenReturn(Optional.of(cliente));
            when(reservaRepository.save(any())).thenReturn(reserva);
            when(reservaMapper.toDTO(any())).thenReturn(response);

            var result = reservaService.cadastrarReserva(dto);

            assertNotNull(result);
            verify(reservaRepository).save(reservaCaptor.capture());
            assertFalse(carro.getDisponivel());
            assertTrue(reservaCaptor.getValue().isStatus());
        }

        @Test
        @DisplayName("Deve lançar exceção quando carro não existir")
        void deveLancarExcecaoQuandoCarroNaoExistir() {
            var dto = new ReservaDTO(1L, 1L, LocalDateTime.now().plusDays(2));
            when(carroRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> reservaService.cadastrarReserva(dto));
        }

        @Test
        @DisplayName("Deve lançar exceção quando cliente não existir")
        void deveLancarExcecaoQuandoClienteNaoExistir() {
            var carro = criarCarroDisponivel();
            var dto = new ReservaDTO(1L, 1L, LocalDateTime.now().plusDays(2));

            when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));
            when(clientRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> reservaService.cadastrarReserva(dto));
        }

        @Test
        @DisplayName("Deve lançar exceção quando carro não estiver disponível")
        void deveLancarExcecaoQuandoCarroNaoDisponivel() {
            var carro = criarCarroDisponivel();
            carro.setDisponivel(false);
            var cliente = criarCliente();
            var dto = new ReservaDTO(1L, 1L, LocalDateTime.now().plusDays(2));

            when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));
            when(clientRepository.findById(1L)).thenReturn(Optional.of(cliente));

            assertThrows(IllegalStateException.class, () -> reservaService.cadastrarReserva(dto));
        }

        @Test
        @DisplayName("Deve lançar exceção quando data de fim estiver no passado")
        void deveLancarExcecaoQuandoDataFimPassada() {
            var carro = criarCarroDisponivel();
            var cliente = criarCliente();
            var dto = new ReservaDTO(1L, 1L, LocalDateTime.now().minusDays(1));

            when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));
            when(clientRepository.findById(1L)).thenReturn(Optional.of(cliente));

            assertThrows(IllegalArgumentException.class, () -> reservaService.cadastrarReserva(dto));
        }
    }


    @Nested
    @DisplayName("Consultar reservas")
    class ConsultarReservas {

        @Test
        @DisplayName("Deve retornar todas as reservas")
        void deveRetornarTodasAsReservas() {
            when(reservaRepository.findAll()).thenReturn(List.of(criarReserva()));
            when(reservaMapper.toListDTO(anyList())).thenReturn(List.of(mock(ReservaResponseDTO.class)));

            var result = reservaService.consultaReserva();

            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Deve consultar reserva por ID com sucesso")
        void deveConsultarReservaPorIdComSucesso() {
            var reserva = criarReserva();
            when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
            when(reservaMapper.toDTO(reserva)).thenReturn(mock(ReservaResponseDTO.class));

            var result = reservaService.consultaReservaPorId(1L);

            assertNotNull(result);
            verify(reservaRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção ao consultar reserva inexistente")
        void deveLancarExcecaoAoConsultarReservaInexistente() {
            when(reservaRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> reservaService.consultaReservaPorId(1L));
        }
    }


    @Nested
    @DisplayName("Deletar reserva")
    class DeletarReserva {

        @Test
        @DisplayName("Deve deletar reserva com sucesso")
        void deveDeletarReservaComSucesso() {
            var reserva = criarReserva();
            reserva.setStatus(false);
            reserva.getCarro().setDisponivel(false);

            when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

            var result = reservaService.deletaReserva(1L);

            assertEquals(ResponseEntity.ok("Reserva excluída com sucesso"), result);
            verify(reservaRepository).delete(reserva);
        }

        @Test
        @DisplayName("Deve lançar exceção ao deletar reserva ativa")
        void deveLancarExcecaoAoDeletarReservaAtiva() {
            var reserva = criarReserva();
            reserva.setStatus(true);
            when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

            assertThrows(IllegalArgumentException.class, () -> reservaService.deletaReserva(1L));
        }

        @Test
        @DisplayName("Deve lançar exceção quando carro ainda estiver disponível")
        void deveLancarExcecaoQuandoCarroAindaDisponivel() {
            var reserva = criarReserva();
            reserva.setStatus(false);
            reserva.getCarro().setDisponivel(true);

            when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

            assertThrows(IllegalArgumentException.class, () -> reservaService.deletaReserva(1L));
        }

        @Test
        @DisplayName("Deve lançar exceção ao deletar reserva inexistente")
        void deveLancarExcecaoAoDeletarReservaInexistente() {
            when(reservaRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> reservaService.deletaReserva(1L));
        }
    }

    @Nested
    @DisplayName("Atualizar reserva")
    class AtualizarReserva {

        @Test
        @DisplayName("Deve ativar reserva inativa com sucesso")
        void deveAtivarReservaInativaComSucesso() {
            var reserva = criarReserva();
            reserva.setStatus(false);
            reserva.getCarro().setDisponivel(true);

            when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
            when(reservaRepository.save(any())).thenReturn(reserva);
            when(reservaMapper.toDTO(any())).thenReturn(mock(ReservaResponseDTO.class));

            var result = reservaService.atualizaReservaPorId(1L);

            assertNotNull(result);
            assertTrue(reserva.isStatus());
            assertFalse(reserva.getCarro().getDisponivel());
        }

        @Test
        @DisplayName("Deve desativar reserva ativa com sucesso")
        void deveDesativarReservaAtivaComSucesso() {
            var reserva = criarReserva();
            reserva.setStatus(true);
            reserva.getCarro().setDisponivel(false);

            when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
            when(reservaRepository.save(any())).thenReturn(reserva);
            when(reservaMapper.toDTO(any())).thenReturn(mock(ReservaResponseDTO.class));

            var result = reservaService.atualizaReservaPorId(1L);

            assertNotNull(result);
            assertFalse(reserva.isStatus());
            assertTrue(reserva.getCarro().getDisponivel());
        }

        @Test
        @DisplayName("Deve lançar exceção ao atualizar reserva inexistente")
        void deveLancarExcecaoAoAtualizarReservaInexistente() {
            when(reservaRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> reservaService.atualizaReservaPorId(1L));
        }
    }
}
