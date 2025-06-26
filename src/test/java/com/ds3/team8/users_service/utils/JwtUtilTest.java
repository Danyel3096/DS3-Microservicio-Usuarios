package com.ds3.team8.users_service.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import com.ds3.team8.users_service.entities.User;
import com.ds3.team8.users_service.enums.Role;
import java.util.Base64;
import java.util.Date;

public class JwtUtilTest {
    private JwtUtil jwtUtil;
    private final String rawSecret = "SuperSecretKeyForJWTShouldBeAtLeast32CharactersLong123!";
    private final User mockUser = new User("Diego", "Tolosa", "diego@example.com", "+573001112233", "Calle 123", Role.CUSTOMER);

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        String base64Secret = Base64.getEncoder().encodeToString(rawSecret.getBytes());
        ReflectionTestUtils.setField(jwtUtil, "secretKey", base64Secret);
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        mockUser.setId(1L);

        String token = jwtUtil.generateToken(mockUser);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Parsear el token manualmente para validar su contenido
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(
                (String) ReflectionTestUtils.getField(jwtUtil, "secretKey")));

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals(mockUser.getId(), ((Number) claims.get("user-id")).longValue());
        assertEquals(mockUser.getEmail(), claims.get("user-email"));
        assertEquals(mockUser.getRole().name(), claims.get("user-role"));

        // Validar expiración dentro del rango esperado
        Date expiration = claims.getExpiration();
        assertTrue(expiration.after(new Date()));
    }
}
