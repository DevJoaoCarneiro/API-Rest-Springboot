package com.example.demo.service;

import com.example.demo.Entities.Carro;
import com.example.demo.Entities.Manutencao;
import com.example.demo.Entities.Pagamento;
import com.example.demo.dto.ManutencaoDTO;
import com.example.demo.dto.ManutencaoResponseDTO;
import com.example.demo.mapper.ManutencaoMapper;
import com.example.demo.repository.ICarroRepository;
import com.example.demo.repository.IManutencaoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManutencaoServiceTest {

    @Mock
    private IManutencaoRepository manutencaoRepository;

    @Mock
    private ICarroRepository carroRepository;

    @Mock
    private ManutencaoMapper manutencaoMapper;

    @InjectMocks
    private ManutencaoService manutencaoService;

    @Captor
    private ArgumentCaptor<Manutencao> manutencaoCaptor;

    private Carro criarCarroDisponivel() {
        var carro = new Carro();
        carro.setId(1L);
        carro.setModelo("Civic");
        carro.setMarca("Honda");
        carro.setDisponivel(true);
        return carro;
    }

    private Carro criarCarroIndisponivel() {
        var carro = criarCarroDisponivel();
        carro.setDisponivel(false);
        return carro;
    }

    private Manutencao criarManutencao() {
        var m = new Manutencao();
        m.setId(1L);
        m.setDescricao("Troca de óleo");
        m.setDataInicio(LocalDateTime.now());
        m.setDataFim(LocalDateTime.now().plusDays(1));
        m.setStatus(true);
        m.setCarro(criarCarroDisponivel());
        return m;
    }


    @Nested
    @DisplayName("Cadastrar manutenção")
    class CadastrarManutencao {

        @Test
        @DisplayName("Deve cadastrar manutenção com sucesso")
        void deveCadastrarManutencaoComSucesso() {
            var carro = criarCarroDisponivel();
            var dto = new ManutencaoDTO(1L, LocalDateTime.now().plusDays(2), "Revisão geral");
            var manutencao = criarManutencao();
            var response = new ManutencaoResponseDTO(
                    manutencao.getDataInicio(),
                    manutencao.getDataFim(),
                    manutencao.getStatus(),
                    manutencao.getDescricao(),
                    null
            );

            when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));
            when(manutencaoRepository.save(any())).thenReturn(manutencao);
            when(manutencaoMapper.toDTO(any())).thenReturn(response);

            var result = manutencaoService.cadastraManutencao(dto);

            assertNotNull(result);
            assertEquals("Troca de óleo", result.descricao());
            verify(manutencaoRepository).save(manutencaoCaptor.capture());
            assertFalse(carro.getDisponivel());
            assertTrue(manutencaoCaptor.getValue().getStatus());
        }

        @Test
        @DisplayName("Deve lançar exceção quando carro não existir")
        void deveLancarExcecaoQuandoCarroNaoExistir() {
            var dto = new ManutencaoDTO(1L, LocalDateTime.now().plusDays(2), "Troca de pneu");
            when(carroRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(IllegalArgumentException.class, () -> manutencaoService.cadastraManutencao(dto));
        }

        @Test
        @DisplayName("Deve lançar exceção quando carro não estiver disponível")
        void deveLancarExcecaoQuandoCarroNaoDisponivel() {
            var carro = criarCarroIndisponivel();
            var dto = new ManutencaoDTO(1L, LocalDateTime.now().plusDays(2), "Troca de pneu");
            when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));

            assertThrows(IllegalStateException.class, () -> manutencaoService.cadastraManutencao(dto));
        }

        @Test
        @DisplayName("Deve lançar exceção quando data de fim estiver no passado")
        void deveLancarExcecaoQuandoDataFimPassada() {
            var carro = criarCarroDisponivel();
            var dto = new ManutencaoDTO(1L, LocalDateTime.now().minusDays(1), "Troca de pneu");
            when(carroRepository.findById(1L)).thenReturn(Optional.of(carro));

            assertThrows(IllegalArgumentException.class, () -> manutencaoService.cadastraManutencao(dto));
        }
    }


    @Nested
    @DisplayName("Consultar manutenção")
    class ConsultarManutencao {

        @Test
        @DisplayName("Deve retornar todas as manutenções")
        void deveRetornarTodasAsManutencoes() {
            when(manutencaoRepository.findAll()).thenReturn(List.of(criarManutencao()));
            when(manutencaoMapper.toListDTO(anyList())).thenReturn(List.of(mock(ManutencaoResponseDTO.class)));

            var result = manutencaoService.consultaTodasManutencoes();

            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Deve consultar manutenção por ID com sucesso")
        void deveConsultarManutencaoPorIdComSucesso() {
            var manutencao = criarManutencao();
            var response = mock(ManutencaoResponseDTO.class);

            when(manutencaoRepository.findById(1L)).thenReturn(Optional.of(manutencao));
            when(manutencaoMapper.toDTO(manutencao)).thenReturn(response);

            var result = manutencaoService.consultaManutencaoPorId(1L);

            assertNotNull(result);
            verify(manutencaoRepository).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção ao consultar manutenção inexistente")
        void deveLancarExcecaoAoConsultarManutencaoInexistente() {
            when(manutencaoRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> manutencaoService.consultaManutencaoPorId(1L));
        }
    }


    @Nested
    @DisplayName("Deletar manutenção")
    class DeletarManutencao {

        @Test
        @DisplayName("Deve deletar manutenção com sucesso")
        void deveDeletarManutencaoComSucesso() {
            var manutencao = criarManutencao();
            manutencao.setStatus(false);
            manutencao.setPagamento(null);
            var carro = manutencao.getCarro();
            when(manutencaoRepository.findById(1L)).thenReturn(Optional.of(manutencao));

            manutencaoService.deletaManutencaoPorId(1L);

            verify(manutencaoRepository).deleteById(1L);
            verify(carroRepository).save(carro);
            assertTrue(carro.getDisponivel());
        }

        @Test
        @DisplayName("Deve lançar exceção ao deletar manutenção ativa")
        void deveLancarExcecaoAoDeletarManutencaoAtiva() {
            var manutencao = criarManutencao();
            manutencao.setStatus(true);
            when(manutencaoRepository.findById(1L)).thenReturn(Optional.of(manutencao));

            assertThrows(IllegalArgumentException.class, () -> manutencaoService.deletaManutencaoPorId(1L));
        }

        @Test
        @DisplayName("Deve lançar exceção ao deletar manutenção com pagamento vinculado")
        void deveLancarExcecaoAoDeletarManutencaoComPagamento() {
            var manutencao = criarManutencao();
            manutencao.setStatus(false);
            manutencao.setPagamento(mock(Pagamento.class));
            when(manutencaoRepository.findById(1L)).thenReturn(Optional.of(manutencao));

            assertThrows(IllegalArgumentException.class, () -> manutencaoService.deletaManutencaoPorId(1L));
        }

        @Test
        @DisplayName("Deve lançar exceção ao deletar manutenção inexistente")
        void deveLancarExcecaoAoDeletarManutencaoInexistente() {
            when(manutencaoRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> manutencaoService.deletaManutencaoPorId(1L));
        }
    }


    @Nested
    @DisplayName("Editar manutenção")
    class EditarManutencao {

        @Test
        @DisplayName("Deve editar manutenção com sucesso")
        void deveEditarManutencaoComSucesso() {
            var manutencao = criarManutencao();
            var dto = new ManutencaoDTO(1L, LocalDateTime.now().plusDays(3), "Troca de filtro");
            var response = mock(ManutencaoResponseDTO.class);

            when(manutencaoRepository.findById(1L)).thenReturn(Optional.of(manutencao));
            when(carroRepository.findById(1L)).thenReturn(Optional.of(manutencao.getCarro()));
            when(manutencaoRepository.save(any())).thenReturn(manutencao);
            when(manutencaoMapper.toDTO(any())).thenReturn(response);

            var result = manutencaoService.editaManutencaoPorId(1L, dto);

            assertNotNull(result);
            verify(manutencaoRepository).save(any());
        }

        @Test
        @DisplayName("Deve lançar exceção ao editar manutenção inexistente")
        void deveLancarExcecaoAoEditarManutencaoInexistente() {
            var dto = new ManutencaoDTO(1L, LocalDateTime.now(), "Teste");
            when(manutencaoRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> manutencaoService.editaManutencaoPorId(1L, dto));
        }
    }


    @Nested
    @DisplayName("Editar status da manutenção")
    class EditarStatus {

        @Test
        @DisplayName("Deve desativar manutenção ativa com sucesso")
        void deveDesativarManutencaoAtivaComSucesso() {
            var manutencao = criarManutencao();
            when(manutencaoRepository.findById(1L)).thenReturn(Optional.of(manutencao));
            when(manutencaoRepository.save(any())).thenReturn(manutencao);
            when(manutencaoMapper.toDTO(any())).thenReturn(mock(ManutencaoResponseDTO.class));

            var result = manutencaoService.editaStatusDaManutencao(1L);

            assertNotNull(result);
            assertFalse(manutencao.getStatus());
            assertTrue(manutencao.getCarro().getDisponivel());
        }

        @Test
        @DisplayName("Deve ativar manutenção inativa com sucesso")
        void deveAtivarManutencaoInativaComSucesso() {
            var manutencao = criarManutencao();
            manutencao.setStatus(false);
            manutencao.getCarro().setDisponivel(true);

            when(manutencaoRepository.findById(1L)).thenReturn(Optional.of(manutencao));
            when(manutencaoRepository.save(any())).thenReturn(manutencao);
            when(manutencaoMapper.toDTO(any())).thenReturn(mock(ManutencaoResponseDTO.class));

            var result = manutencaoService.editaStatusDaManutencao(1L);

            assertNotNull(result);
            assertTrue(manutencao.getStatus());
            assertFalse(manutencao.getCarro().getDisponivel());
        }

        @Test
        @DisplayName("Deve lançar exceção ao tentar editar status de manutenção inexistente")
        void deveLancarExcecaoAoEditarStatusDeManutencaoInexistente() {
            when(manutencaoRepository.findById(1L)).thenReturn(Optional.empty());
            assertThrows(IllegalArgumentException.class, () -> manutencaoService.editaStatusDaManutencao(1L));
        }
    }
}
