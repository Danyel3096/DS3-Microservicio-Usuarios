package com.ds3.team8.users_service.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ds3.team8.users_service.entities.User;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET}")
    private String secretKey;

    private final long jwtExpiration;

    public JwtUtil() {
        this.jwtExpiration = 30L * 60000; // 30 minutos
    }

    // Generar Token con información del usuario
    public String generateToken(User user) {
        return buildToken(user, jwtExpiration);
    }

    // Obtener tiempo de expiración
    public long getExpirationTime() {
        return jwtExpiration;
    }

    // Construir el token
    private String buildToken(User user, long expiration) {
        return Jwts.builder()
                .claim("user-id", user.getId())
                .claim("user-email", user.getEmail())
                .claim("user-role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Obtener clave secreta para firmar el token
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extraer una propiedad específica del token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extraer todos los claims del token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Verificar si el token ha expirado
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}