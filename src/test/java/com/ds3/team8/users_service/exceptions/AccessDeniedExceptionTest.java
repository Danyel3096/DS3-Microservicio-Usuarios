package com.ds3.team8.users_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class AccessDeniedExceptionTest {
    @Test
    void constructor_shouldSetMessageCorrectly() {
        String message = "Acceso denegado";
        AccessDeniedException exception = new AccessDeniedException(message);

        assertEquals(message, exception.getMessage());
    }
}
