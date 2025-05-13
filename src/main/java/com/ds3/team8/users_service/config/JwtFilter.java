package com.ds3.team8.users_service.config;

import com.ds3.team8.users_service.exceptions.UnauthorizedException;
import com.ds3.team8.users_service.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // Extraer la información del usuario del token
                String email = jwtUtil.extractEmail(token);
                Long userId = jwtUtil.extractUserId(token);
                String role = jwtUtil.extractRole(token);

                // Validar el token
                if (jwtUtil.validateToken(token, email)) {
                    // Crear las autoridades
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                    // Crear el objeto de autenticación
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userId, null, authorities);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Establecer el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    logger.warn("Token inválido para el usuario: {}", email);
                    throw new UnauthorizedException("Token inválido o expirado");
                }

            } catch (ExpiredJwtException e) {
                logger.warn("Token expirado: {}", e.getMessage());
                throw new UnauthorizedException("El token ha expirado");
            } catch (MalformedJwtException e) {
                logger.error("Token JWT malformado: {}", e.getMessage());
                throw new UnauthorizedException("Formato del token JWT incorrecto");
            } catch (SignatureException e) {
                logger.error("Firma del token JWT inválida: {}", e.getMessage());
                throw new UnauthorizedException("Firma del token JWT inválida");
            } catch (IllegalArgumentException e) {
                logger.error("Argumento ilegal al procesar el token: {}", e.getMessage());
                throw new UnauthorizedException("Error interno al procesar el token");
            } catch (Exception e) {
                logger.error("Error inesperado al procesar el token: {}", e.getMessage());
                throw new UnauthorizedException("Error al procesar el token");
            }
        }

        filterChain.doFilter(request, response);
    }
}