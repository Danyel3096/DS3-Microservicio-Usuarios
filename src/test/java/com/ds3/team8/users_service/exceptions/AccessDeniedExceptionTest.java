package com.ds3.team8.users_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AccessDeniedExceptionTest {
    @Test
    void testMessageConstructor() {
        String message = "Acceso denegado";
        AccessDeniedException exception = new AccessDeniedException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testMessageAndCauseConstructor() {
        String message = "Acceso denegado";
        Throwable cause = new RuntimeException("Causa interna");

        AccessDeniedException exception = new AccessDeniedException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testCauseConstructor() {
        Throwable cause = new RuntimeException("Causa directa");

        AccessDeniedException exception = new AccessDeniedException(cause);

        assertEquals(cause, exception.getCause());
    }
}
