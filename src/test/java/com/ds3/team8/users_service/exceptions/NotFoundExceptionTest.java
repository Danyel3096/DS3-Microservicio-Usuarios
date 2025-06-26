package com.ds3.team8.users_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class NotFoundExceptionTest {
    @Test
    void constructor_shouldSetMessageCorrectly() {
        String message = "Recurso no encontrado";
        NotFoundException exception = new NotFoundException(message);

        assertEquals(message, exception.getMessage());
    }
}
