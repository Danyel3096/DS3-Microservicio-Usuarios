package com.ds3.team8.users_service.config;

import com.ds3.team8.users_service.exceptions.UnauthorizedException;
import com.ds3.team8.users_service.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
                // Extraer el email del token para validarlo
                String email = jwtUtil.extractEmail(token);

                // Validar el token
                if (jwtUtil.validateToken(token, email)) {
                    // Extraer el userId y el rol del token
                    Long userId = jwtUtil.extractUserId(token);
                    String role = jwtUtil.extractRole(token);

                    // Convertir el rol a GrantedAuthority
                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                    // Crear el objeto de autenticación
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userId, null, authorities);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Establecer el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    throw new UnauthorizedException("Token inválido o expirado");
                }
            } catch (Exception e) {
                // Manejar excepciones relacionadas con el token
                throw new UnauthorizedException("Error al procesar el token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}