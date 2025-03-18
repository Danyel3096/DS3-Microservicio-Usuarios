package com.ds3.team8.users_service.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Maneja cualquier excepción genérica no controlada en la aplicación
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        // Se crea un mapa para estructurar la respuesta de error
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Error inesperado: " + ex.getMessage()); // Mensaje de error con detalles
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // Código HTTP 500

        // Retorna la respuesta con el estado 500 (Internal Server Error)
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Maneja errores específicos relacionados con la base de datos (Spring DataAccessException)
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException ex) {
        // Se crea un mapa para estructurar la respuesta de error
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Error al acceder a la base de datos."); // Mensaje genérico
        // Se concatena el mensaje de error con la causa más específica para más detalles
        response.put("error", ex.getMessage().concat(": ").concat(ex.getMostSpecificCause().getMessage()));

        // Retorna la respuesta con el estado 500 (Internal Server Error)
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
