package com.example.demo.service;

import com.example.demo.Entities.Carro;
import com.example.demo.Entities.Cliente;
import com.example.demo.Entities.Reserva;
import com.example.demo.dto.CarroDTO;
import com.example.demo.dto.CarroResponseDTO;
import com.example.demo.dto.ReservaDTO;
import com.example.demo.mapper.CarroMapper;
import com.example.demo.repository.ICarroRepository;
import com.example.demo.repository.IReservaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarroServiceTest {

    @Mock
    private ICarroRepository carroRepository;

    @Mock
    private IReservaRepository reservaRepository;

    @Mock
    private CarroMapper carroMapper;

    @InjectMocks
    private CarroService carroService;

    @Captor
    private ArgumentCaptor<Carro> carroCaptor;

    private Carro criarCarro() {
        var carro = new Carro();
        carro.setId(1L);
        carro.setModelo("Civic");
        carro.setMarca("Honda");
        carro.setAno(2020);
        carro.setPlaca("ABC-1234");
        carro.setPrecoDiaria(BigDecimal.valueOf(150));
        carro.setDisponivel(true);
        carro.setReservas(new ArrayList<>());
        return carro;
    }

    private CarroDTO criarCarroDTO() {
        return new CarroDTO("Civic", "Honda", 2020, "ABC-1234", BigDecimal.valueOf(150));
    }

    private Cliente criarCliente() {
        var cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João");
        return cliente;
    }

    private Reserva criarReserva(Carro carro, Cliente cliente) {
        var reserva = new Reserva();
        reserva.setId(1L);
        reserva.setCarro(carro);
        reserva.setCliente(cliente);
        reserva.setDataFim(LocalDate.now().plusDays(2).atStartOfDay());
        reserva.setStatus(true);
        return reserva;
    }


    @Nested
    @DisplayName("Cadastrar novo carro")
    class CadastrarCarro {

        @Test
        @DisplayName("Deve cadastrar novo carro com sucesso")
        void deveCadastrarNovoCarroComSucesso() {
            var dto = criarCarroDTO();
            var carro = criarCarro();

            when(carroMapper.toEntity(dto)).thenReturn(carro);
            when(carroRepository.save(any())).thenReturn(carro);
            when(carroMapper.toDTO(carro)).thenReturn(dto);

            var result = carroService.cadastraNovoCarro(dto);

            assertNotNull(result);
            assertEquals(dto.modelo(), result.modelo());
            verify(carroRepository).save(carroCaptor.capture());
            assertTrue(carroCaptor.getValue().getDisponivel());
        }
    }


    @Nested
    @DisplayName("Consultar todos os carros")
    class ConsultarCarros {

        @Test
        @DisplayName("Deve retornar lista de carros com sucesso")
        void deveRetornarListaDeCarrosComSucesso() {
            var carro = criarCarro();
            var cliente = criarCliente();
            var reserva = criarReserva(carro, cliente);

            when(carroRepository.findAllByOrderByIdAsc()).thenReturn(List.of(carro));
            when(reservaRepository.findByStatus(true)).thenReturn(List.of(reserva));
            when(carroMapper.toDTO(carro)).thenReturn(criarCarroDTO());

            var result = carroService.consultarTodosOsCarros();

            assertEquals(1, result.size());
            assertEquals("Civic", result.get(0).carro().modelo());
            assertNotNull(result.get(0).reserva());
        }

        @Test
        @DisplayName("Deve retornar carros sem reservas ativas")
        void deveRetornarCarrosSemReservasAtivas() {
            var carro = criarCarro();

            when(carroRepository.findAllByOrderByIdAsc()).thenReturn(List.of(carro));
            when(reservaRepository.findByStatus(true)).thenReturn(List.of());
            when(carroMapper.toDTO(carro)).thenReturn(criarCarroDTO());

            var result = carroService.consultarTodosOsCarros();

            assertEquals(1, result.size());
            assertNull(result.get(0).reserva());
        }
    }


    @Nested
    @DisplayName("Consultar carro por ID")
    class ConsultarCarroPorId {

        @Test
        @DisplayName("Deve consultar carro por ID com sucesso e reserva ativa")
        void deveConsultarCarroPorIdComReservaAtiva() {
            var carro = criarCarro();
            var cliente = criarCliente();
            var reserva = criarReserva(carro, cliente);

            when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));
            when(reservaRepository.findByCarroIdAndStatus(1L, true)).thenReturn(Optional.of(reserva));
            when(carroMapper.toDTO(carro)).thenReturn(criarCarroDTO());

            var result = carroService.consultaCarroPorId(1L);

            assertEquals(1L, result.id());
            assertNotNull(result.reserva());
        }

        @Test
        @DisplayName("Deve consultar carro por ID sem reserva ativa")
        void deveConsultarCarroPorIdSemReservaAtiva() {
            var carro = criarCarro();

            when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));
            when(reservaRepository.findByCarroIdAndStatus(1L, true)).thenReturn(Optional.empty());
            when(carroMapper.toDTO(carro)).thenReturn(criarCarroDTO());

            var result = carroService.consultaCarroPorId(1L);

            assertEquals(1L, result.id());
            assertNull(result.reserva());
        }

        @Test
        @DisplayName("Deve lançar exceção ao consultar carro inexistente")
        void deveLancarExcecaoAoConsultarCarroInexistente() {
            when(carroRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> carroService.consultaCarroPorId(1L));
        }
    }


    @Nested
    @DisplayName("Filtrar carros por disponibilidade")
    class FiltrarCarros {

        @Test
        @DisplayName("Deve retornar carros disponíveis com sucesso")
        void deveRetornarCarrosDisponiveisComSucesso() {
            var carro = criarCarro();
            when(carroRepository.findByDisponivelTrueOrderByIdAsc()).thenReturn(List.of(carro));
            when(carroMapper.toDTOList(anyList())).thenReturn(List.of(criarCarroDTO()));

            var result = carroService.filtraCarroPorReservas();

            assertEquals(1, result.size());
            assertEquals("Civic", result.get(0).modelo());
        }

        @Test
        @DisplayName("Deve lançar exceção quando não houver carros disponíveis")
        void deveLancarExcecaoQuandoNaoHouverCarrosDisponiveis() {
            when(carroRepository.findByDisponivelTrueOrderByIdAsc()).thenReturn(List.of());
            assertThrows(IllegalArgumentException.class, () -> carroService.filtraCarroPorReservas());
        }
    }


    @Nested
    @DisplayName("Deletar carro")
    class DeletarCarro {

        @Test
        @DisplayName("Deve deletar carro por ID com sucesso")
        void deveDeletarCarroComSucesso() {
            var carro = criarCarro();
            carro.setReservas(new ArrayList<>());
            when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));

            carroService.deletaCarroPorId(1L);

            verify(carroRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar deletar carro com reservas")
        void deveLancarExcecaoAoTentarDeletarCarroComReservas() {
            var carro = criarCarro();
            carro.setReservas(List.of(mock(Reserva.class)));
            when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));

            assertThrows(IllegalStateException.class, () -> carroService.deletaCarroPorId(1L));
            verify(carroRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar deletar carro inexistente")
        void deveLancarExcecaoAoTentarDeletarCarroInexistente() {
            when(carroRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> carroService.deletaCarroPorId(1L));
        }
    }


    @Nested
    @DisplayName("Editar carro")
    class EditarCarro {

        @Test
        @DisplayName("Deve editar carro com sucesso")
        void deveEditarCarroComSucesso() {
            var carro = criarCarro();
            var dto = new CarroDTO("HRV", "Honda", 2022, "XYZ-5678", BigDecimal.valueOf(200));

            when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));
            when(carroRepository.save(any())).thenReturn(carro);
            when(carroMapper.toDTO(any())).thenReturn(dto);

            var result = carroService.editaCarroPorId(1L, dto);

            assertNotNull(result);
            assertEquals("HRV", result.modelo());
        }

        @Test
        @DisplayName("Deve lançar exceção ao editar carro inexistente")
        void deveLancarExcecaoAoEditarCarroInexistente() {
            var dto = criarCarroDTO();
            when(carroRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> carroService.editaCarroPorId(1L, dto));
        }

        @Test
        @DisplayName("Deve lançar exceção ao editar carro indisponível")
        void deveLancarExcecaoAoEditarCarroIndisponivel() {
            var carro = criarCarro();
            carro.setDisponivel(false);

            var dto = criarCarroDTO();
            when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));

            assertThrows(IllegalStateException.class, () -> carroService.editaCarroPorId(1L, dto));
        }
    }
}
