package com.example.demo.controller;

import com.example.demo.Entities.Cliente;
import com.example.demo.Entities.embedded.Endereco;
import com.example.demo.dto.ClienteDTO;
import com.example.demo.dto.ConsultaClienteDTO;
import com.example.demo.dto.PatchClienteDTO;
import com.example.demo.service.ClienteService;
import com.example.demo.repository.IClientRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private IClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCadastrarCliente() throws Exception {

        Endereco endereco = new Endereco("Rua A", "87000000", "Zona 7", "Casa", "123");

        ClienteDTO request = new ClienteDTO(
                "João Victor",
                "joao@email.com",
                "44999999999",
                endereco,
                "12345678900"
        );

        Mockito.when(clienteService.cadastraCliente(any()))
                .thenReturn(request);

        mockMvc.perform(
                        post("/api/cliente")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("João Victor"))
                .andExpect(jsonPath("$.email").value("joao@email.com"));
    }


    @Test
    void deveListarClientes() throws Exception {

        ConsultaClienteDTO cliente = new ConsultaClienteDTO(
                1L, "Carlos", "carlos@email.com", "44999999999",
                new Endereco("Rua A", "87000", "Centro", "", "10")
        );

        Mockito.when(clienteService.consultaCliente())
                .thenReturn(List.of(cliente));

        mockMvc.perform(
                        get("/api/cliente")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Carlos"));
    }


    @Test
    void deveBuscarClientePorId() throws Exception {

        Cliente cliente = new Cliente(
                1L,
                "Ana",
                "ana@email.com",
                "44999999999",
                new Endereco("Rua B", "87000", "Centro", "", "20"),
                "12345678900",
                List.of()
        );

        Mockito.when(clienteService.buscaClientePorId(1L))
                .thenReturn(org.springframework.http.ResponseEntity.ok(cliente));

        mockMvc.perform(get("/api/cliente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Ana"));
    }


    @Test
    void deveEditarCliente() throws Exception {

        PatchClienteDTO request = new PatchClienteDTO(
                "João Atualizado",
                null,
                null,
                null
        );

        ConsultaClienteDTO atualizado = new ConsultaClienteDTO(
                1L,
                "João Atualizado",
                "email@teste.com",
                "44999999999",
                null
        );

        Mockito.when(clienteService.editaCliente(eq(1L), any()))
                .thenReturn(atualizado);

        mockMvc.perform(
                        patch("/api/cliente/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Atualizado"));
    }

    // ---------------------------------------------------------
    // DELETE - deletaCliente
    // ---------------------------------------------------------
    @Test
    void deveDeletarCliente() throws Exception {

        Mockito.when(clienteService.deletaCliente(1L))
                .thenReturn(ResponseEntity.noContent().build());

        mockMvc.perform(delete("/api/cliente/1"))
                .andExpect(status().isNoContent());
    }
}
