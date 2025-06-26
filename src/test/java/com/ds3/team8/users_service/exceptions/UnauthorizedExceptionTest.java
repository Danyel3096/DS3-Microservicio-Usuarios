package com.ds3.team8.users_service.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UnauthorizedExceptionTest {
    @Test
    void constructor_shouldSetMessageCorrectly() {
        String message = "No autorizado";
        UnauthorizedException exception = new UnauthorizedException(message);

        assertEquals(message, exception.getMessage());
    }
}
