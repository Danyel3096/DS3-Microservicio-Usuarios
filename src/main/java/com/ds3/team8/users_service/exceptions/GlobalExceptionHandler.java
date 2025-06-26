package com.ds3.team8.users_service.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String ERROR_KEY = "error";
    private static final String STATUS_KEY = "status";

    // Maneja excepciones de recursos no encontrados
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex) {
        // Se crea un mapa para estructurar la respuesta de error
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR_KEY, ex.getMessage()); // Mensaje de error específico
        response.put(STATUS_KEY, HttpStatus.NOT_FOUND.value()); // Código HTTP 404

        // Retorna la respuesta con el estado 404 (Not Found)
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Maneja excepciones de solicitud incorrecta
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequestException(BadRequestException ex) {
        // Se crea un mapa para estructurar la respuesta de error
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR_KEY, ex.getMessage()); // Mensaje de error específico
        response.put(STATUS_KEY, HttpStatus.BAD_REQUEST.value()); // Código HTTP 400

        // Retorna la respuesta con el estado 400 (Bad Request)
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Maneja excepciones de acceso no autorizado
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException ex) {
        // Se crea un mapa para estructurar la respuesta de error
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR_KEY, ex.getMessage()); // Mensaje de error específico
        response.put(STATUS_KEY, HttpStatus.UNAUTHORIZED.value()); // Código HTTP 401

        // Retorna la respuesta con el estado 401 (Unauthorized)
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Maneja excepciones de acceso prohibido
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException ex) {
        // Se crea un mapa para estructurar la respuesta de error
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR_KEY, ex.getMessage()); // Mensaje de error específico
        response.put(STATUS_KEY, HttpStatus.FORBIDDEN.value()); // Código HTTP 403

        // Retorna la respuesta con el estado 403 (Forbidden)
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Maneja excepciones de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS_KEY, HttpStatus.BAD_REQUEST.value());

        // Extraer errores específicos de cada campo
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Maneja cualquier excepción genérica no controlada en la aplicación
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        // Se crea un mapa para estructurar la respuesta de error
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR_KEY, "Error inesperado: " + ex.getMessage()); // Mensaje de error con detalles
        response.put(STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value()); // Código HTTP 500

        // Retorna la respuesta con el estado 500 (Internal Server Error)
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Maneja errores específicos relacionados con la base de datos
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Map<String, Object>> handleDataAccessException(DataAccessException ex) {
        // Se crea un mapa para estructurar la respuesta de error
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR_KEY, "Error de acceso a datos: " + ex.getMessage()); // Mensaje de error con detalles
        response.put(STATUS_KEY, HttpStatus.INTERNAL_SERVER_ERROR.value()); // Código HTTP 500
        // Retorna la respuesta con el estado 500 (Internal Server Error)
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Usuario tiene órdenes asociadas
    @ExceptionHandler(UserHasOrdersException.class)
    public ResponseEntity<Map<String, Object>> handleUserHasOrdersException(UserHasOrdersException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR_KEY, ex.getMessage());
        response.put(STATUS_KEY, HttpStatus.CONFLICT.value()); // 409 Conflict
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Error al verificar las órdenes
    @ExceptionHandler(OrderVerificationException.class)
    public ResponseEntity<Map<String, Object>> handleOrderVerificationException(OrderVerificationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR_KEY, ex.getMessage());
        response.put(STATUS_KEY, HttpStatus.SERVICE_UNAVAILABLE.value()); // 503 Service Unavailable
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }

    // Usuario tiene entregas asociadas
    @ExceptionHandler(UserHasDeliveriesException.class)
    public ResponseEntity<Map<String, Object>> handleUserHasDeliveriesException(UserHasDeliveriesException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR_KEY, ex.getMessage());
        response.put(STATUS_KEY, HttpStatus.CONFLICT.value()); // 409 Conflict
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Error al verificar las entregas
    @ExceptionHandler(DeliveryVerificationException.class)
    public ResponseEntity<Map<String, Object>> handleDeliveryVerificationException(DeliveryVerificationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put(ERROR_KEY, ex.getMessage());
        response.put(STATUS_KEY, HttpStatus.SERVICE_UNAVAILABLE.value()); // 503 Service Unavailable
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
