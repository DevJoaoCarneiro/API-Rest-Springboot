package com.example.demo.service;


import com.example.demo.Entities.Usuario;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.mapper.UsuarioMapper;
import com.example.demo.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioMapper usuarioMapper;
    

    public List<UsuarioDTO> listarTodos() {
        return usuarioMapper.toDTOList(usuarioRepository.findAll());
    }

    public Optional<UsuarioDTO> buscarPorId(Long id) {
        return usuarioRepository.findById(id).map(usuarioMapper::toDTO);
    }

    public UsuarioDTO salvar(UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    public void deletar(Long id) {
        usuarioRepository.deleteById(id);
    }
}

