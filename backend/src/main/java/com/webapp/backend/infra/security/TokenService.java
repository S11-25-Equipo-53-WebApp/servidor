package com.webapp.backend.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.webapp.backend.Entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${TOKEN_PASSWORD}")
    private String apiSecret;

    // ------------------------------
    // GENERAR TOKEN
    // ------------------------------
    public String generarToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);

            return JWT.create()
                    .withIssuer("webapp")
                    .withSubject(user.getEmail())     // Subject debe ser username
                    .withClaim("id", user.getId())
                    .withClaim("role", user.getUserType().name())
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
