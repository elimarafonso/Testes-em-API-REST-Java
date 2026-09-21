package com.techdesk.techdesk.security.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.techdesk.techdesk.usuarios.entity.Usuario;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-minutes:1}")
    private long expirationMinutes;
    
    
    public String generateToken(Usuario user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("techdesk-chamados-api")
                    .withSubject(user.getLogin())
                    .withClaim("role", user.getRole().name())
                    .withExpiresAt(gerarDataExpiracao())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("techdesk-chamados-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null; // inválido, expirado ou adulterado
        }
    }

    private Instant gerarDataExpiracao() {
    	Instant agora = Instant.now();
    	return agora.plus(expirationMinutes,ChronoUnit.MINUTES) ;//LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}






















