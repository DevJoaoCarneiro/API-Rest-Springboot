package com.example.demo.service;

import com.example.demo.Entities.Carro;
import com.example.demo.Entities.Manutencao;
import com.example.demo.dto.ManutencaoDTO;
import com.example.demo.dto.ManutencaoResponseDTO;
import com.example.demo.mapper.ManutencaoMapper;
import com.example.demo.repository.ICarroRepository;
import com.example.demo.repository.IManutencaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ManutencaoService {

    private IManutencaoRepository manutencaoRepository;

    private ICarroRepository carroRepository;

    private ManutencaoMapper manutencaoMapper;

    public ManutencaoResponseDTO cadastraManutencao(ManutencaoDTO manutencaoDTO) {
        Carro carro = carroRepository.findById(manutencaoDTO.carro_id())
                .orElseThrow(() -> new IllegalArgumentException("Id do carro " + manutencaoDTO.carro_id() + " Nao encontrado"));


        if (!carro.getDisponivel()) {
            throw new IllegalStateException("O carro selecionado não está disponível.");
        }

        if (manutencaoDTO.dataFim().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data de fim não pode estar no passado.");
        }

        Manutencao manutencao = new Manutencao();
        manutencao.setDataInicio(LocalDateTime.now());
        manutencao.setDataFim(manutencaoDTO.dataFim());
        manutencao.setStatus(true);
        manutencao.setDescricao(manutencaoDTO.descricao());
        manutencao.setCarro(carro);
        carro.setDisponivel(false);

        manutencaoRepository.save(manutencao);

        return manutencaoMapper.toDTO(manutencao);

    }

    public List<ManutencaoResponseDTO> consultaTodasManutencoes() {
        return manutencaoMapper.toListDTO(manutencaoRepository.findAll());
    }

    public ManutencaoResponseDTO consultaManutencaoPorId(Long id) {
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manutenção com o id " + id + "não foi encontrado"));

        return manutencaoMapper.toDTO(manutencao);
    }

    public void deletaManutencaoPorId(Long id) {
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Manutencao com o id " + id + " não foi encontrada"));

        if (manutencao.getStatus()) {
            throw new IllegalArgumentException("Você não pode excluir uma manutanção ativa");
        }

        if (manutencao.getPagamento() != null) {
            throw new IllegalArgumentException("Não é possível excluir uma manutenção que possui pagamento vinculado");
        }

        Carro carro = manutencao.getCarro();
        if (carro != null) {
            carro.setDisponivel(true);
            carroRepository.save(carro);
        }

        manutencaoRepository.deleteById(id);
    }

    public ManutencaoResponseDTO editaManutencaoPorId(Long id, ManutencaoDTO manutencaoDTO) {
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Manutencao com o id " + id + " não foi encontrada"));

        if (manutencaoDTO.carro_id() != null) {
            Carro carro = carroRepository.findById(manutencaoDTO.carro_id())
                    .orElseThrow(() -> new IllegalArgumentException("Erro ao encontrar o id do carro"));
            manutencao.setCarro(carro);
        }

        if (manutencaoDTO.dataFim() != null) {
            manutencao.setDataFim(manutencaoDTO.dataFim());
        }

        if (manutencaoDTO.descricao() != null) {
            manutencao.setDescricao(manutencaoDTO.descricao());
        }

        return manutencaoMapper.toDTO(manutencaoRepository.save(manutencao));
    }

    public ManutencaoResponseDTO editaStatusDaManutencao(Long id){
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma manutenção com esse ID foi encontrada"));

        if(manutencao.getStatus()){
            manutencao.getCarro().setDisponivel(true);
            manutencao.setStatus(false);
        }else{
            manutencao.getCarro().setDisponivel(false);
            manutencao.setStatus(true);
        }

        return manutencaoMapper.toDTO(manutencaoRepository.save(manutencao));
    }

}
