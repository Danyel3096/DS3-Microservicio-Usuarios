package com.ds3.team8.users_service.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.ds3.team8.users_service.exceptions.UnauthorizedException;
import com.ds3.team8.users_service.enums.Role;

public class SecurityUtilTest {
    // -------- validateRole --------

    @Test
    void validateRole_shouldPass_whenRoleIsAllowed() {
        assertDoesNotThrow(() -> 
            SecurityUtil.validateRole("CUSTOMER", Role.CUSTOMER, Role.ADMIN));
    }

    @Test
    void validateRole_shouldThrow_whenRoleIsNotAllowed() {
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
            SecurityUtil.validateRole("DRIVER", Role.CUSTOMER, Role.ADMIN));
        assertEquals("El usuario no tiene permisos suficientes.", exception.getMessage());
    }

    @Test
    void validateRole_shouldThrow_whenRoleIsNull() {
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
            SecurityUtil.validateRole(null, Role.CUSTOMER));
        assertEquals("Rol del usuario no especificado.", exception.getMessage());
    }

    // -------- parseUserId --------

    @Test
    void parseUserId_shouldReturnLong_whenValid() {
        Long id = SecurityUtil.parseUserId("123");
        assertEquals(123L, id);
    }

    @Test
    void parseUserId_shouldThrow_whenNull() {
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
            SecurityUtil.parseUserId(null));
        assertEquals("ID del usuario no especificado.", exception.getMessage());
    }

    @Test
    void parseUserId_shouldThrow_whenInvalid() {
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
            SecurityUtil.parseUserId("abc"));
        assertEquals("ID de usuario inválido.", exception.getMessage());
    }
}
