package com.ds3.team8.users_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class NotFoundExceptionTest {
    @Test
    void testMessageConstructor() {
        String message = "Recurso no encontrado";
        NotFoundException exception = new NotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testMessageAndCauseConstructor() {
        String message = "Recurso no encontrado";
        Throwable cause = new RuntimeException("Causa interna");

        NotFoundException exception = new NotFoundException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testCauseConstructor() {
        Throwable cause = new RuntimeException("Causa directa");

        NotFoundException exception = new NotFoundException(cause);

        assertEquals(cause, exception.getCause());
    }
}
