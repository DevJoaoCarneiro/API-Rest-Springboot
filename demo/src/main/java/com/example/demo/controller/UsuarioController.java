package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.UsuarioDTO;
import com.example.demo.service.UsuarioService;
import com.example.demo.service.Utils.ApiResponse;
import com.example.demo.service.Utils.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Lista todos os usuários", description = "Retorna uma lista com todos os usuários cadastrados")
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @Operation(summary = "Busca um usuário por ID", description = "Retorna os detalhes de um usuário específico")
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        Optional<UsuarioDTO> usuarioDTO = usuarioService.buscarPorId(id);
        return usuarioDTO.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cria um novo usuário", description = "Cadastra um novo usuário no sistema")
    @PostMapping
    public ResponseEntity<ApiResponse<UsuarioDTO>> criarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            // Tenta salvar o usuário
            UsuarioDTO savedUsuario = usuarioService.salvar(usuarioDTO);
            
            // Retorna sucesso com o UsuarioDTO salvo
            ApiResponse<UsuarioDTO> response = new ApiResponse<>(savedUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // Cria um erro com a mensagem específica
            ErrorResponse errorResponse = new ErrorResponse("Argumento inválido", e.getMessage());
            ApiResponse<UsuarioDTO> response = new ApiResponse<>(errorResponse);
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            // Cria um erro genérico
            ErrorResponse errorResponse = new ErrorResponse("Erro interno", e.getMessage());
            ApiResponse<UsuarioDTO> response = new ApiResponse<>(errorResponse);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "Deleta um usuário", description = "Remove um usuário do sistema pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
