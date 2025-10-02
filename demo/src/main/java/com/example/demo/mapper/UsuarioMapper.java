package com.example.demo.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.demo.Entities.Usuario;
import com.example.demo.dto.UsuarioDTO;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    

    UsuarioDTO toDTO(Usuario usuario);

    Usuario toEntity(UsuarioDTO usuarioDTO);

    List<UsuarioDTO> toDTOList(List<Usuario> usuarios);
}
