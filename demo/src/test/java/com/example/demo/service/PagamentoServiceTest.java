package com.example.demo.service;

import com.example.demo.Entities.Manutencao;
import com.example.demo.Entities.Pagamento;
import com.example.demo.Entities.Reserva;
import com.example.demo.Entities.embedded.Status;
import com.example.demo.dto.EditaPagamentoDto;
import com.example.demo.dto.PagamentoDTO;
import com.example.demo.dto.PagamentoResponseDTO;
import com.example.demo.mapper.PagamentoMapper;
import com.example.demo.repository.IManutencaoRepository;
import com.example.demo.repository.IPagamentoRepository;
import com.example.demo.repository.IReservaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PagamentoServiceTest {

    @Mock
    private IPagamentoRepository pagamentoRepository;

    @Mock
    private IReservaRepository reservaRepository;

    @Mock
    private IManutencaoRepository manutencaoRepository;

    @Mock
    private PagamentoMapper pagamentoMapper;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Pagamento pagamento;
    private PagamentoDTO pagamentoReservaDTO;
    private PagamentoDTO pagamentoManutencaoDTO;
    private Reserva reserva;
    private Manutencao manutencao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reserva = new Reserva();
        reserva.setId(1L);

        manutencao = new Manutencao();
        manutencao.setId(2L);

        pagamento = new Pagamento();
        pagamento.setId(10L);
        pagamento.setValor(BigDecimal.valueOf(500));
        pagamento.setFormaPagamento("PIX");
        pagamento.setStatus(Status.PAGO);

        pagamentoReservaDTO = new PagamentoDTO(
                null,
                1L,
                BigDecimal.valueOf(500),
                "PIX"
        );

        pagamentoManutencaoDTO = new PagamentoDTO(
                2L,
                null,
                BigDecimal.valueOf(300),
                "Cartão"
        );
    }

    @Nested
    class cadastraPagamento {

        @Test
        @DisplayName("Deve cadastrar um pagamento com uma reserva")
        void deveCadastrarPagamentoComReserva() {
            when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));
            when(pagamentoRepository.existsByReservaId(1L)).thenReturn(false);
            when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);
            when(pagamentoMapper.toDTO(any(Pagamento.class))).thenReturn(mock(PagamentoResponseDTO.class));

            PagamentoResponseDTO result = pagamentoService.cadastroPagamento(pagamentoReservaDTO);

            assertThat(result).isNotNull();
            verify(pagamentoRepository).save(any(Pagamento.class));
            verify(pagamentoMapper).toDTO(any(Pagamento.class));
        }


        @Test
        @DisplayName("Deve cadastrar um pagamento com uma manutenção")
        void deveCadastrarPagamentoComManutencao() {
            when(manutencaoRepository.findById(2L)).thenReturn(Optional.of(manutencao));
            when(pagamentoRepository.existsByManutencaoId(2L)).thenReturn(false);
            when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);
            when(pagamentoMapper.toDTO(any(Pagamento.class))).thenReturn(mock(PagamentoResponseDTO.class));

            PagamentoResponseDTO result = pagamentoService.cadastroPagamento(pagamentoManutencaoDTO);

            assertThat(result).isNotNull();
            verify(pagamentoRepository).save(any(Pagamento.class));
        }

        @Test
        @DisplayName("Deve lançar um erro quando a reserva ou manutenção forem informadas juntas")
        void deveLancarErroQuandoReservaEManutencaoInformadasJuntas() {
            PagamentoDTO dto = new PagamentoDTO(2L, 1L, BigDecimal.valueOf(200), "PIX");

            assertThatThrownBy(() -> pagamentoService.cadastroPagamento(dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Informe apenas uma referência");
        }

        @Test
        @DisplayName("Deve lançar erro quando nenhuma referencia é informada")
        void deveLancarErroQuandoNenhumaReferenciaInformada() {
            PagamentoDTO dto = new PagamentoDTO(null, null, BigDecimal.valueOf(200), "PIX");

            assertThatThrownBy(() -> pagamentoService.cadastroPagamento(dto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("É necessário informar uma reserva");
        }

        @Test
        @DisplayName("Deve lançar um erro quando a reserva possuir um pagamento ativo")
        void deveLancarErroQuandoReservaJaPossuiPagamentoAtivo() {
            when(pagamentoRepository.existsByReservaId(1L)).thenReturn(true);

            assertThatThrownBy(() -> pagamentoService.cadastroPagamento(pagamentoReservaDTO))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("já possui um pagamento ativo");
        }

        @Test
        @DisplayName("Deve lançar um erro quando a manutenção possuir um pagamento ativo")
        void deveLancarErroQuandoManutencaoJaPossuiPagamentoAtivo() {
            when(pagamentoRepository.existsByManutencaoId(2L)).thenReturn(true);

            assertThatThrownBy(() -> pagamentoService.cadastroPagamento(pagamentoManutencaoDTO))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("já possui um pagamento ativo");
        }

        @Test
        @DisplayName("Deve lançar um erro quando não encontrar uma reserva")
        void deveLancarErroQuandoReservaNaoEncontrada() {
            when(pagamentoRepository.existsByReservaId(1L)).thenReturn(false);
            when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> pagamentoService.cadastroPagamento(pagamentoReservaDTO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Reserva não encontrada");
        }

        @Test
        @DisplayName("Deve lançar um erro quando não encontrar uma manutenção")
        void deveLancarErroQuandoManutencaoNaoEncontrada() {
            when(pagamentoRepository.existsByManutencaoId(2L)).thenReturn(false);
            when(manutencaoRepository.findById(2L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> pagamentoService.cadastroPagamento(pagamentoManutencaoDTO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Manutenção não encontrada");
        }
    }

    @Nested
    class consultaPagamento{
        @Test
        @DisplayName("Deve consultar todos os pagamentos cadastrados")
        void deveConsultarTodosOsPagamentosCadastrados() {
            when(pagamentoRepository.findAll()).thenReturn(List.of(pagamento));
            when(pagamentoMapper.toListDTO(anyList())).thenReturn(
                    List.of(new PagamentoResponseDTO(
                            1L,
                            Status.PAGO,
                            BigDecimal.valueOf(500),
                            LocalDateTime.now(),
                            "PIX",
                            "Reserva",
                            null
                    ))
            );

            List<PagamentoResponseDTO> result = pagamentoService.consultarPagamento();

            assertThat(result).isNotEmpty();
            verify(pagamentoRepository).findAll();
            verify(pagamentoMapper).toListDTO(anyList());
        }
    }

    @Nested
    class EditaPagamentoTests {
        @Test
        void deveEditarPagamentoComSucesso() {
            EditaPagamentoDto dto = new EditaPagamentoDto(BigDecimal.valueOf(999), "Crédito");
            when(pagamentoRepository.findById(10L)).thenReturn(Optional.of(pagamento));
            when(pagamentoRepository.save(any())).thenReturn(pagamento);

            EditaPagamentoDto result = pagamentoService.editaPagamento(10L, dto);

            assertThat(result.valor()).isEqualTo(BigDecimal.valueOf(999));
            assertThat(result.formaPagamento()).isEqualTo("Crédito");
            verify(pagamentoRepository).save(any());
        }

        @Test
        void deveLancarErroQuandoPagamentoNaoEncontrado() {
            when(pagamentoRepository.findById(99L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> pagamentoService.editaPagamento(99L, new EditaPagamentoDto(null, "PIX")))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    class AtualizaStatusPagamentoTests {

        @Test
        void deveAtualizarStatusComSucesso() {
            pagamento.setStatus(Status.PENDENTE);
            when(pagamentoRepository.findById(10L)).thenReturn(Optional.of(pagamento));
            when(pagamentoRepository.save(any())).thenReturn(pagamento);
            when(pagamentoMapper.toDTO(any())).thenReturn(
                    new PagamentoResponseDTO(
                            1L,
                            Status.PAGO,
                            BigDecimal.valueOf(500),
                            LocalDateTime.now(),
                            "PIX",
                            "Reserva",
                            null
                    )
            );

            PagamentoResponseDTO result = pagamentoService.atualizaStatusDoPagamento(10L, Status.PAGO);

            assertThat(result).isNotNull();
            verify(pagamentoRepository).save(any());
            verify(pagamentoMapper).toDTO(any());
        }

        @Test
        void deveLancarErroQuandoPagamentoNaoEncontrado() {
            when(pagamentoRepository.findById(10L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> pagamentoService.atualizaStatusDoPagamento(10L, Status.PAGO))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Pagamento não encontrado");
        }

        @Test
        void deveLancarErroQuandoPagamentoCancelado() {
            pagamento.setStatus(Status.CANCELADO);
            when(pagamentoRepository.findById(10L)).thenReturn(Optional.of(pagamento));
            assertThatThrownBy(() -> pagamentoService.atualizaStatusDoPagamento(10L, Status.PAGO))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("cancelados não podem");
        }

        @Test
        void deveLancarErroQuandoPagamentoJaPago() {
            pagamento.setStatus(Status.PAGO);
            when(pagamentoRepository.findById(10L)).thenReturn(Optional.of(pagamento));
            assertThatThrownBy(() -> pagamentoService.atualizaStatusDoPagamento(10L, Status.PENDENTE))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("já pagos");
        }

        @Test
        void deveLancarErroQuandoStatusNaoMuda() {
            pagamento.setStatus(Status.PENDENTE);
            when(pagamentoRepository.findById(10L)).thenReturn(Optional.of(pagamento));
            assertThatThrownBy(() -> pagamentoService.atualizaStatusDoPagamento(10L, Status.PENDENTE))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("já está com o status");
        }
    }
}
