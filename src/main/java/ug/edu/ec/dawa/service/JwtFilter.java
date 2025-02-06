package ug.edu.ec.dawa.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // Inyección del utilitario para manejo de JWT

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Obtener el encabezado de autorización
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Eliminar el prefijo "Bearer "
            try {
                // Validar el token
                if (jwtUtil.validateToken(token)) {
                    // Obtener las claims (datos contenidos en el token)
                    Claims claims = Jwts.parserBuilder()
                            .setSigningKey(jwtUtil.getKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();

                    // Ejemplo: recupera roles/authorities desde las claims, asumamos que hay una claim 'roles'
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    String roles = claims.get("roles", String.class);
                    if (roles != null) {
                        for (String role : roles.split(",")) {
                            authorities.add(new SimpleGrantedAuthority(role));
                        }
                    }

                    // Establecer la autenticación en el contexto de seguridad
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // Añadir las claims al request para que puedan ser usadas posteriormente
                    request.setAttribute("claims", claims);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid JWT token.");
                return;
            }
        }

        // Continuar con el filtro siguiente
        filterChain.doFilter(request, response);
    }
}
