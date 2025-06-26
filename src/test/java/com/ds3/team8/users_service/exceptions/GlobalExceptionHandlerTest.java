package com.ds3.team8.users_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import java.util.Map;
import java.util.List;

class GlobalExceptionHandlerTest {
    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFoundException_shouldReturn404() {
        NotFoundException ex = new NotFoundException("No encontrado");
        ResponseEntity<Map<String, Object>> response = handler.handleNotFoundException(ex);

        assertEquals(404, response.getStatusCode().value());
        assertEquals("No encontrado", response.getBody().get("error"));
    }

    @Test
    void handleBadRequestException_shouldReturn400() {
        BadRequestException ex = new BadRequestException("Solicitud inválida");
        ResponseEntity<Map<String, Object>> response = handler.handleBadRequestException(ex);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Solicitud inválida", response.getBody().get("error"));
    }

    @Test
    void handleUnauthorizedException_shouldReturn401() {
        UnauthorizedException ex = new UnauthorizedException("No autorizado");
        ResponseEntity<Map<String, Object>> response = handler.handleUnauthorizedException(ex);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("No autorizado", response.getBody().get("error"));
    }

    @Test
    void handleAccessDeniedException_shouldReturn403() {
        AccessDeniedException ex = new AccessDeniedException("Acceso denegado");
        ResponseEntity<Map<String, Object>> response = handler.handleAccessDeniedException(ex);

        assertEquals(403, response.getStatusCode().value());
        assertEquals("Acceso denegado", response.getBody().get("error"));
    }

    @Test
    void handleGeneralException_shouldReturn500() {
        Exception ex = new RuntimeException("Error interno");
        ResponseEntity<Map<String, Object>> response = handler.handleGeneralException(ex);

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().get("error").toString().contains("Error inesperado"));
    }

    @Test
    void handleDataAccessException_shouldReturn500() {
        DataAccessException ex = new DataIntegrityViolationException("Violación de integridad");
        ResponseEntity<Map<String, Object>> response = handler.handleDataAccessException(ex);

        assertEquals(500, response.getStatusCode().value());
        assertTrue(response.getBody().get("error").toString().contains("Error de acceso a datos"));
    }

    @Test
    void handleValidationExceptions_shouldReturn400() {
        BindingResult bindingResult = mock(BindingResult.class);
        List<FieldError> fieldErrors = List.of(
            new FieldError("object", "email", "El correo es inválido"),
            new FieldError("object", "password", "La contraseña es obligatoria")
        );
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<Map<String, Object>> response = handler.handleValidationExceptions(ex);

        assertEquals(400, response.getStatusCode().value());
        Map<String, String> errors = (Map<String, String>) response.getBody().get("errors");
        assertEquals("El correo es inválido", errors.get("email"));
        assertEquals("La contraseña es obligatoria", errors.get("password"));
    }
}
