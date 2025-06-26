package com.ds3.team8.users_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UnauthorizedExceptionTest {
    @Test
    void testMessageConstructor() {
        String message = "No autorizado";
        UnauthorizedException exception = new UnauthorizedException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testMessageAndCauseConstructor() {
        String message = "Token inválido";
        Throwable cause = new RuntimeException("Token expirado");

        UnauthorizedException exception = new UnauthorizedException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testCauseConstructor() {
        Throwable cause = new RuntimeException("Fallo en autenticación");

        UnauthorizedException exception = new UnauthorizedException(cause);

        assertEquals(cause, exception.getCause());
    }
}
