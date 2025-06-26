package com.ds3.team8.users_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;



import org.junit.jupiter.api.Test;

class BadRequestExceptionTest {
    @Test
    void testMessageConstructor() {
        String message = "Error de solicitud";
        BadRequestException exception = new BadRequestException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void testMessageAndCauseConstructor() {
        String message = "Error de solicitud";
        Throwable cause = new RuntimeException("Causa interna");

        BadRequestException exception = new BadRequestException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testCauseConstructor() {
        Throwable cause = new RuntimeException("Causa directa");

        BadRequestException exception = new BadRequestException(cause);

        assertEquals(cause, exception.getCause());
    }
}
