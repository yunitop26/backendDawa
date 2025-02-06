package ug.edu.ec.dawa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ug.edu.ec.dawa.service.JwtFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class securityConfig {

    private final JwtFilter jwtFilter;

    public securityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configuración CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/document/*").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/odoo/**").permitAll()
                        .requestMatchers("/api/v1/communications/historical/topic/**").permitAll()
                        .requestMatchers(request ->
                                request.getRequestURI().contains("swagger-ui") ||
                                        request.getRequestURI().contains("api-docs") ||
                                        request.getRequestURI().contains("swagger-resources"))
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Agregar filtro JWT

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*")); // Permitir todos los orígenes
        configuration.addAllowedHeader("*"); // Permitir todos los encabezados
        configuration.addAllowedMethod("*"); // Permitir todos los métodos HTTP
        configuration.setAllowCredentials(false); // No permitir credenciales

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
