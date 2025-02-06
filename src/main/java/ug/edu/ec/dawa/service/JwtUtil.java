package ug.edu.ec.dawa.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "+P4NnRRcpyrk6o9Do357jBXi8WjiGEz1yNVm8cCy/XI="; // Usa una clave segura
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 8; // 8 horas

    private final Key key;


    public JwtUtil() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        this.key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(String username, String userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId) // Añadir el session_id como claim
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public UserDetails extractUserDetails(String token) {
        try {
            // Analizamos y verificamos el token para obtener los claims
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key) // Validar el token con la clave secreta
                    .build()
                    .parseClaimsJws(token) // Extraer el cuerpo del token
                    .getBody();

            // Extraer el nombre de usuario del `subject` del token
            String username = claims.getSubject();

            // Extraer el userId del claim personalizado (debe estar incluido en el token)
            String userId = claims.get("userId", String.class);

            // [Opcional] Mostrar toda la información contenida en el JWT (para depuración)
            //claims.forEach((key, value) -> System.out.println(key + ": " + value));

            // Retornar un objeto UserDetails que contiene username y userId
            return new UserDetails(username, userId);
        } catch (Exception e) {
            // Capturamos cualquier error en el token (expirado, inválido, etc.)
            return null;
        }
    }


    public static class UserDetails {
        private final String username;
        private final String userId; // Ahora incluye el userId

        public UserDetails(String username, String userId) {
            this.username = username;
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public String getUserId() {
            return userId;
        }

        @Override
        public String toString() {
            return "UserDetails{" +
                    "username='" + username + '\'' +
                    ", userId='" + userId + '\'' + // Se actualiza para reflejar el userId
                    '}';
        }
    }




    public Key getKey() {
        return key;
    }
}


