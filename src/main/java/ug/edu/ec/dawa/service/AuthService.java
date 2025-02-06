package ug.edu.ec.dawa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ug.edu.ec.dawa.entity.User;
import ug.edu.ec.dawa.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository; // Repositorio de usuarios (asume que existe)

    @Autowired
    private JwtUtil jwtUtil; // Utilidad para manejar JWT


    public Map<String, String> login(String username, String password) {
        // Buscar al usuario en la base de datos
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar la contraseña (sin encriptar)
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Generar el token JWT con el username y otros datos relevantes
        String token = jwtUtil.generateToken(user.getUsername(), user.getUsername()); // Puedes cambiar el segundo parámetro si tienes un ID único

        // Preparar la respuesta
        Map<String, String> response = new HashMap<>();
        response.put("token", token);// Retornar el token JWT
        response.put("username", user.getUsername());
        response.put("role", user.getRole()); // Retornar el rol del usuario
        response.put("userId", String.valueOf(user.getId()));//Agregar el userId como string

        return response; // Retornar el token y el rol
    }


}