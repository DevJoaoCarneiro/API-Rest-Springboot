package com.example.demo.service;

import com.example.demo.Entities.Manutencao;
import com.example.demo.Entities.Pagamento;
import com.example.demo.Entities.Reserva;
import com.example.demo.dto.PagamentoDTO;
import com.example.demo.dto.PagamentoResponseDTO;
import com.example.demo.mapper.PagamentoMapper;
import com.example.demo.repository.IManutencaoRepository;
import com.example.demo.repository.IPagamentoRepository;
import com.example.demo.repository.IReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
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
        pagamento.setStatus(true);

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

    @Test
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
    void deveLancarErroQuandoReservaEManutencaoInformadasJuntas() {
        PagamentoDTO dto = new PagamentoDTO(2L, 1L, BigDecimal.valueOf(200), "PIX");

        assertThatThrownBy(() -> pagamentoService.cadastroPagamento(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Informe apenas uma referência");
    }

    @Test
    void deveLancarErroQuandoNenhumaReferenciaInformada() {
        PagamentoDTO dto = new PagamentoDTO(null, null, BigDecimal.valueOf(200), "PIX");

        assertThatThrownBy(() -> pagamentoService.cadastroPagamento(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("É necessário informar uma reserva");
    }

    @Test
    void deveLancarErroQuandoReservaJaPossuiPagamentoAtivo() {
        when(pagamentoRepository.existsByReservaId(1L)).thenReturn(true);

        assertThatThrownBy(() -> pagamentoService.cadastroPagamento(pagamentoReservaDTO))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("já possui um pagamento ativo");
    }

    @Test
    void deveLancarErroQuandoManutencaoJaPossuiPagamentoAtivo() {
        when(pagamentoRepository.existsByManutencaoId(2L)).thenReturn(true);

        assertThatThrownBy(() -> pagamentoService.cadastroPagamento(pagamentoManutencaoDTO))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("já possui um pagamento ativo");
    }

    @Test
    void deveLancarErroQuandoReservaNaoEncontrada() {
        when(pagamentoRepository.existsByReservaId(1L)).thenReturn(false);
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagamentoService.cadastroPagamento(pagamentoReservaDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Reserva não encontrada");
    }

    @Test
    void deveLancarErroQuandoManutencaoNaoEncontrada() {
        when(pagamentoRepository.existsByManutencaoId(2L)).thenReturn(false);
        when(manutencaoRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pagamentoService.cadastroPagamento(pagamentoManutencaoDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Manutenção não encontrada");
    }
}
