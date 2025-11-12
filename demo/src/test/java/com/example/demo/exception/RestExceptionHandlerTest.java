package com.example.demo.exception;

import com.example.demo.dto.ApiErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Mock
    private HttpServletRequest request;


    @Nested
    @DisplayName("EntityNotFoundException")
    class EntityNotFound {

        @Test
        @DisplayName("Deve retornar 404 e mensagem apropriada")
        void deveRetornar404QuandoEntidadeNaoEncontrada() {
            when(request.getRequestURI()).thenReturn("/clientes/1");

            EntityNotFoundException ex = new EntityNotFoundException("Cliente não encontrado");

            ResponseEntity<ApiErrorResponseDTO> response = handler.handleEntityNotFound(ex, request);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals(404, response.getBody().status());
            assertEquals("Recurso não encontrado", response.getBody().error());
            assertEquals("Cliente não encontrado", response.getBody().message());
            assertEquals("/clientes/1", response.getBody().path());
            assertNotNull(response.getBody().timestamp());
        }
    }


    @Nested
    @DisplayName("IllegalArgumentException")
    class IllegalArgument {

        @Test
        @DisplayName("Deve retornar 400 e mensagem de requisição inválida")
        void deveRetornar400QuandoArgumentoInvalido() {
            when(request.getRequestURI()).thenReturn("/carros");
            IllegalArgumentException ex = new IllegalArgumentException("Campo obrigatório ausente");

            ResponseEntity<ApiErrorResponseDTO> response = handler.handleIllegalArgument(ex, request);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals(400, response.getBody().status());
            assertEquals("Requisição inválida", response.getBody().error());
            assertEquals("Campo obrigatório ausente", response.getBody().message());
        }
    }


    @Nested
    @DisplayName("IllegalStateException")
    class IllegalState {

        @Test
        @DisplayName("Deve retornar 409 e mensagem de conflito")
        void deveRetornar409QuandoConflitoDeEstado() {
            when(request.getRequestURI()).thenReturn("/reservas");
            IllegalStateException ex = new IllegalStateException("Carro já reservado");

            ResponseEntity<ApiErrorResponseDTO> response = handler.handleIllegalState(ex, request);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertEquals(409, response.getBody().status());
            assertEquals("Conflito de estado", response.getBody().error());
            assertEquals("Carro já reservado", response.getBody().message());
        }
    }


    @Nested
    @DisplayName("MethodArgumentNotValidException")
    class Validation {

        @Mock
        private BindingResult bindingResult;

        @Test
        @DisplayName("Deve retornar 400 com mensagem do campo inválido")
        void deveRetornar400ComMensagemDeCampoInvalido() {
            when(request.getRequestURI()).thenReturn("/clientes");
            FieldError fieldError = new FieldError("ClienteDTO", "email", "Formato de e-mail inválido");
            when(bindingResult.getFieldError()).thenReturn(fieldError);

            MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<ApiErrorResponseDTO> response = handler.handleValidationError(ex, request);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Erro de validação", response.getBody().error());
            assertEquals("Formato de e-mail inválido", response.getBody().message());
        }

        @Test
        @DisplayName("Deve retornar mensagem padrão quando não houver fieldError")
        void deveRetornarMensagemPadraoQuandoSemFieldError() {
            when(request.getRequestURI()).thenReturn("/clientes");
            when(bindingResult.getFieldError()).thenReturn(null);

            MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

            ResponseEntity<ApiErrorResponseDTO> response = handler.handleValidationError(ex, request);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Erro de validação nos campos enviados", response.getBody().message());
        }
    }


    @Nested
    @DisplayName("Exception genérica")
    class GenericException {

        @Test
        @DisplayName("Deve retornar 500 e mensagem de erro interno")
        void deveRetornar500QuandoErroGenerico() {
            when(request.getRequestURI()).thenReturn("/api");
            Exception ex = new Exception("Erro inesperado");

            ResponseEntity<ApiErrorResponseDTO> response = handler.handleGenericException(ex, request);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertEquals(500, response.getBody().status());
            assertEquals("Erro interno no servidor", response.getBody().error());
            assertEquals("Erro inesperado", response.getBody().message());
            assertEquals("/api", response.getBody().path());
            assertTrue(response.getBody().timestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
        }
    }
}
