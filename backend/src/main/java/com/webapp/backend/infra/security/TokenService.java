package com.webapp.backend.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.webapp.backend.Entities.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${TOKEN_PASSWORD}")
    private String apiSecret;

    // ------------------------------
    // GENERAR TOKEN
    // ------------------------------
    public String generarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);

            return JWT.create()
                    .withIssuer("webapp")
                    .withSubject(usuario.getEmail())     // Subject debe ser username
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaDeExpiracion())
                    .sign(algorithm);

        } catch (JWTCreationException e) {
            throw new RuntimeException("Error al generar token", e);
        }
    }

    // Duración 2 horas
    private Instant generarFechaDeExpiracion() {
        return Instant.now().plus(Duration.ofHours(2));
    }

    // ------------------------------
    // VALIDAR TOKEN Y OBTENER SUBJECT
    // ------------------------------
    public String getSubject(String token) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);

            var decoded = JWT.require(algorithm)
                    .withIssuer("webapp")
                    .build()
                    .verify(token);

            return decoded.getSubject();

        } catch (JWTVerificationException e) {
            throw new RuntimeException("Token inválido o expirado", e);
        }
    }
}
